package dairaga.akka

import akka.actor.{ActorContext, ActorRef, ActorSystem, Address, Props, Terminated}
import akka.cluster.Cluster
import akka.cluster.client.ClusterClient.Publish
import akka.cluster.pubsub.DistributedPubSubMediator.Subscribe
import dairaga.env._
import org.slf4j.LoggerFactory

import scala.collection.immutable

/**
  * Dairaga akka cluster node.
  *
  * A dairaga akka cluster node has an intern actor to manage communication between nodes and manage node life cycle.
  *
  * Created by kigi on 5/8/17.
  */
trait ClusterNode {

  private val log = LoggerFactory.getLogger(classOf[ClusterNode])

  /**
    * Configuration resource name
    */
  private var _resource: String = ""

  /**
    * Node status
    *
    * 0x00 is initialized.
    * 0x01 is started (after invoking run).
    * 0x02 is terminated.
    */
  private var _status: Byte = 0x00

  /**
    * Node is initialized or not.
    * @return
    */
  final def initialized: Boolean = _status == 0x00

  /**
    * Node is started or not.
    * @return
    */
  final def started: Boolean = _status == 0x01

  /**
    * Node is terminated or not.
    * @return
    */
  final def terminated: Boolean = _status == 0x02


  private[dairaga] lazy val cluster: Cluster = AkkaUtils.cluster(seeds, _resource)

  private[dairaga] lazy val system: ActorSystem = cluster.system

  private[dairaga] lazy val intern: ActorRef = system.actorOf(Props(new DairagaActor {

    mediator ! Subscribe(ClusterInfo, self)
    mediator ! Publish(ClusterInfo, XVRegister)

    override def receive: Receive = {
      case XVPing =>
        sender() ! XVRegister

      case XVShutdown =>
        shutdown()

      case Terminated(terminatedActor) =>
        context.unwatch(terminatedActor)
        childOnTerminated(context, terminatedActor)
    }

    override def preStart(): Unit = {
      super.preStart()
      internPreStart(context)
    }

    override def postStop(): Unit = {
      internPostStop(context)
      super.postStop()
    }

  }), InternActor/* + dairaga.common.TextUtils.randomAlphaNumeric(16)*/)

  def seeds: immutable.Seq[Address]

  /* ClusterNode hook functions start */

  /**
    * It is called when starting a node
    */
  @throws(classOf[Exception])
  def preStart(): Unit = Unit

  /**
    * It is called when node is terminated.
    */
  @throws(classOf[Exception])
  def postStop(): Unit = Unit

  /**
    * It is called when node started.
    */
  @throws(classOf[Exception])
  def afterRun(): Unit = Unit
  /* ClusterNode hook functions end */

  /* Intern Actor hook functions start */

  /**
    * It is called when intern actor started
    * @param context
    */
  @throws(classOf[Exception])
  def internPreStart(context: ActorContext): Unit = Unit

  /**
    * It is called when intern actor stopped.
    * @param context
    * @throws java.lang.Exception
    */
  @throws(classOf[Exception])
  def internPostStop(context: ActorContext): Unit = Unit

  /**
    * It is called when some child actor is terminated.
    *
    * The terminated child actor must be watched by intern before.
    *
    * @param context
    * @param actor
    * @throws java.lang.Exception
    */
  @throws(classOf[Exception])
  def childOnTerminated(context: ActorContext, actor: ActorRef): Unit = Unit
  /* Intern Actor hook functions end */


  /**
    * Start a node
    *
    * @param resourceName
    */
  def run(resourceName: String = ""): Unit = {

    _resource = resourceName

    preStart()

    system.registerOnTermination(postStop)

    log.info(s"intern - ${intern.path.toStringWithAddress(cluster.selfAddress)} started")

    _status = 0x01
    afterRun()
  }

  /**
    * Shutdown a node
    */
  def shutdown(): Unit = {
    if (!terminated) {
      AkkaUtils.close(cluster).onComplete { _ =>
        _status = 0x02
        postStop()
      }(system.dispatcher)
    }
  }
}
