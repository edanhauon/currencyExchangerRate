import com.shenkar.currency.control.CurrencyMainController;
import com.shenkar.currency.model.CurrencyDao;
import com.shenkar.currency.model.CurrencyXMLUpdater;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.charset.Charset;

import static com.sun.org.apache.xalan.internal.utils.SecuritySupport.getResourceAsStream;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyXMLUpdaterTest {
    private CurrencyXMLUpdater currencyXMLUpdater;

    @Mock
    private CurrencyDao currencyDao;

    @Mock
    private CurrencyMainController currencyMainController;

    private String staticXMLString;

    @Before
    public void setUp() {
        try {
            staticXMLString = IOUtils.toString(getResourceAsStream("static.xml"), Charset.defaultCharset());
        } catch (IOException e) {
            System.err.println("Problem with reading the static file");
        }
    }

    public CurrencyXMLUpdaterTest() {
        currencyXMLUpdater = new CurrencyXMLUpdater(currencyDao, currencyMainController);
    }

    @Test
    public void testXMLUpdater() {
    }

}