package telegram;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ImageCreate {

Messages msg = new Messages();

    public void fileImageCreate(Long chatID, List<String> titleOfSpheres, Map<String, Object> resultsForUser) {

        String user = String.valueOf(chatID);

        DefaultPieDataset dataset = new DefaultPieDataset( );
        dataset.setValue("Параметр 1", new Double( 20 ) );
        dataset.setValue("Параметр 2", new Double( 20 ) );
        dataset.setValue("Параметр 3", new Double( 40 ) );
        dataset.setValue("Параметр 4", new Double( 10 ) );
        dataset.setValue("Параметр 5", new Double( 10 ) );
        dataset.setValue("Параметр 6", new Double( 20 ) );
        dataset.setValue("Параметр 7", new Double( 10 ) );
        dataset.setValue("Параметр 8", new Double( 10 ) );

        JFreeChart chart = ChartFactory.createPieChart(
                "Название",   // chart title
                dataset,          // data
                false,             // include legend
                false,
                false);

        int width = 1000;   /* Width of the image */
        int height = 720;  /* Height of the image */


        File imageChart = new File( msg.PATH_TO_FILES + user + msg.JPEG_IMAGE_FILE);

        try {
            ChartUtilities.saveChartAsJPEG( imageChart , chart , width , height );
        }
        catch (Exception e) {
            System.out.println("\nПроизошла ошибка во время создания файла-изображения : " + e.toString());;
        }

    }
}
