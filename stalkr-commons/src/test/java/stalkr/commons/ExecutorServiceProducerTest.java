package stalkr.commons;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doReturn;
import kikaha.core.cdi.ProviderContext;
import lombok.val;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith( MockitoJUnitRunner.class )
public class ExecutorServiceProducerTest {

	@Mock
	ProviderContext context;

	@Mock
	Name nameAnnotation;

	@Before
	public void setupContext() {
		doReturn( nameAnnotation ).when( context ).getAnnotation( Name.class );
		doReturn( "executor-01" ).when( nameAnnotation ).value();
	}

	@Test
	public void ensureThatIsAbleToCreateExecutorsAndReuseThem() {
		val producer = new ExecutorServiceProducer();
		val executor = producer.produceExecutorService( context );
		assertNotNull( executor );
		assertSame( executor, producer.produceExecutorService( context ) );
	}
}
