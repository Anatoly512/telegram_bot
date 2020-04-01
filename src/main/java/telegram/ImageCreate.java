package telegram;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.HorizontalAlignment;


import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;

public class ImageCreate {

private Messages msg = new Messages();


    public synchronized void fileImageCreate(Long chatID, List<String> titleOfSpheres, Map<String, Object> resultsForUser) {

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
                dataset,                   // data
                false,
                false,
                false);


        int width = 1000;   /* Width of the image */
        int height = 720;  /* Height of the image */


        // Определение фона графического изображения
        chart.setBackgroundPaint(new GradientPaint(new Point(0, 0),
                new Color(80, 190, 200),
                new Point(400, 200),
                Color.WHITE));

        // Определение заголовка
        TextTitle t = chart.getTitle();
        t.setHorizontalAlignment(HorizontalAlignment.LEFT);
        t.setPaint(new Color(170, 10, 90));
        t.setFont(new Font("Arial", Font.BOLD, 18));


        // Настройка меток названий секций
        PiePlot plot = (PiePlot) chart.getPlot();

        //  Цвета секций
        plot.setSectionPaint(titleOfSpheres.get(0), Color.RED);
        plot.setSectionPaint(titleOfSpheres.get(1), Color.CYAN);
        plot.setSectionPaint(titleOfSpheres.get(2), Color.MAGENTA);
        plot.setSectionPaint(titleOfSpheres.get(3), Color.BLUE);
        plot.setSectionPaint(titleOfSpheres.get(4), Color.YELLOW);
        plot.setSectionPaint(titleOfSpheres.get(5), Color.GREEN);
        plot.setSectionPaint(titleOfSpheres.get(6), new Color(255, 105, 180));
        plot.setSectionPaint(titleOfSpheres.get(7), new Color(0, 250, 150));


        plot.setBackgroundPaint(null);
        plot.setInteriorGap(0.04);
        plot.setOutlineVisible(false);

        plot.setLabelFont(new Font("Courier New", Font.BOLD, 23));
        plot.setLabelLinkPaint(new Color(180, 10, 230));
        plot.setLabelLinkStroke(new BasicStroke(3.0f));
        plot.setLabelOutlineStroke(null);
        plot.setLabelPaint(Color.BLACK);
        plot.setLabelBackgroundPaint(new Color(127, 255, 212));



        File imageChart = new File( msg.PATH_TO_FILES + user + msg.JPEG_IMAGE_FILE);

        try {
            ChartUtilities.saveChartAsJPEG( imageChart , chart , width , height );
        }
        catch (Exception e) {
            System.out.println("\nПроизошла ошибка во время создания файла-изображения : " + e.toString());
        }

    }


}
