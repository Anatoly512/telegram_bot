package telegram;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.io.File;

public class ImageCreate {

    public void fileImageCreate(Long chatID) throws Exception {

        String user = String.valueOf(chatID);

        DefaultPieDataset dataset = new DefaultPieDataset( );
        dataset.setValue("Параметр 1", new Double( 20 ) );
        dataset.setValue("Параметр 2", new Double( 20 ) );
        dataset.setValue("Параметр 3", new Double( 40 ) );
        dataset.setValue("Параметр 4", new Double( 10 ) );

        JFreeChart chart = ChartFactory.createPieChart(
                "Название",   // chart title
                dataset,          // data
                true,             // include legend
                true,
                false);

        int width = 800;   /* Width of the image */
        int height = 600;  /* Height of the image */

        File imageChart = new File( "src/main/java/telegram/user_results/user_" + user + "_results.jpeg" );
        ChartUtilities.saveChartAsJPEG( imageChart , chart , width , height );
    }
}
