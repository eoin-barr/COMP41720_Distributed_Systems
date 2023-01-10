import java.time.Duration;

import org.junit.Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import akka.actor.Props;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;

import service.actor.Init;
import service.actor.Quoter;
import service.core.ClientInfo;
import service.girlpower.GPQService;
import service.messages.QuotationRequest;
import service.messages.QuotationResponse;

public class GirlpowerUnitTest {
    // Declaring static variable sys of type ActorSystem
    static ActorSystem sys;

    // Test setup
    @BeforeClass
    public static void setup() {
        try {
            // Creating the actor system
            sys = ActorSystem.create();
        } catch(Exception e) {
            // Print any errors
            System.out.println("Exception: " + e);
        }
    }

    // Test teardown
    @AfterClass
    public static void teardown() {
        try {
            // Shutting down the actor system
            TestKit.shutdownActorSystem(sys);
            // Setting the system variable to null
            sys = null;
        }catch (Exception e) {
            // Print any errors
            System.out.println("Exception: " + e);
        }
    }

    @Test
    public void testQuoter() { 
        try {
            // Creating the test ref and passing it to the actor system
            ActorRef ref = sys.actorOf(Props.create(Quoter.class), "test");
            // Create a new TestKit instance
            TestKit probe = new TestKit(sys);
            // Initialising the GPQService
            ref.tell(new Init(new GPQService()), null);
            // Sending a quotation request to the test ref
            ref.tell(new QuotationRequest(1, new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 5, 3, "COL123/3")), probe.getRef());
            // Expecting a QuotationResponse message
            probe.awaitCond(probe::msgAvailable);
            probe.expectMsgClass(Duration.ofSeconds(5), QuotationResponse.class); 
        } catch (Exception e) {
            // Handle any errors
            e.printStackTrace();
        }
    }
}
