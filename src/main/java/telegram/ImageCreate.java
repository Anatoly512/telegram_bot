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

        DefaultPieDataset dataset = new DefaultPieDataset();

        dataset.setValue(titleOfSpheres.get(0), (int) resultsForUser.get(msg.BUTTON_1));
        dataset.setValue(titleOfSpheres.get(1), (int) resultsForUser.get(msg.BUTTON_2));
        dataset.setValue(titleOfSpheres.get(2), (int) resultsForUser.get(msg.BUTTON_3));
        dataset.setValue(titleOfSpheres.get(3), (int) resultsForUser.get(msg.BUTTON_4));
        dataset.setValue(titleOfSpheres.get(4), (int) resultsForUser.get(msg.BUTTON_5));
        dataset.setValue(titleOfSpheres.get(5), (int) resultsForUser.get(msg.BUTTON_6));
        dataset.setValue(titleOfSpheres.get(6), (int) resultsForUser.get(msg.BUTTON_7));
        dataset.setValue(titleOfSpheres.get(7), (int) resultsForUser.get(msg.BUTTON_8));


        JFreeChart chart = ChartFactory.createPieChart(
                "Ваши сферы жизни",   // chart title
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
