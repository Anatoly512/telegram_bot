package telegram;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import javax.swing.JPanel;


public class ViewerResults extends ApplicationFrame {


public ViewerResults (String title) {
    super(title);
    setContentPane(createDemoPanel( ));
}


    public void showResults() {
        ViewerResults demo = new ViewerResults( "Заглавие" );
        demo.setSize( 800 , 600 );
        RefineryUtilities.centerFrameOnScreen( demo );
        demo.setVisible( true );
    }

    private PieDataset createDataset( ) {
        DefaultPieDataset dataset = new DefaultPieDataset( );
        dataset.setValue( "Параметр 1" , new Double( 20 ) );
        dataset.setValue( "Параметр 2" , new Double( 20 ) );
        dataset.setValue( "Параметр 3" , new Double( 40 ) );
        dataset.setValue( "Параметр 4" , new Double( 10 ) );
        return dataset;
    }

    private JFreeChart createChart( PieDataset dataset ) {
        JFreeChart chart = ChartFactory.createPieChart(
                "Название",   // chart title
                dataset,          // data
                true,             // include legend
                true,
                false);

        return chart;
    }

    public JPanel createDemoPanel( ) {
        JFreeChart chart = createChart(createDataset( ) );
        return new ChartPanel( chart );
    }


}
