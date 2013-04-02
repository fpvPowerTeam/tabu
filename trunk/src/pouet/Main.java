package pouet;

import java.io.IOException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.dataset.ArrayDataSet;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;

public class Main {
    
    private static void drawPlots(Data data, int nQueens)
	    throws NumberFormatException, ArrayIndexOutOfBoundsException,
	    IOException {
	
	ArrayDataSet dataset_times = new ArrayDataSet(data.getTimes());
	ArrayDataSet dataset_runs = new ArrayDataSet(data.getRuns());
	ArrayDataSet dataset_iter = new ArrayDataSet(data.getIterations());
	
	final JavaPlot p_times = new JavaPlot();
	final JavaPlot p_runs = new JavaPlot();
	final JavaPlot p_iter = new JavaPlot();
	
	PlotStyle style = new PlotStyle();
        style.setStyle(Style.LINES);
	
	DataSetPlot dsp_times = new DataSetPlot(dataset_times);
	dsp_times.setPlotStyle(style);
	DataSetPlot dsp_runs = new DataSetPlot(dataset_runs);
	dsp_runs.setPlotStyle(style);
	DataSetPlot dsp_iter = new DataSetPlot(dataset_iter);
	dsp_iter.setPlotStyle(style);
	
	p_times.setTitle("Execution times (" + nQueens + " queens)");
	p_times.addPlot(dsp_times);
	p_times.setKey(JavaPlot.Key.OFF);
	p_runs.setTitle("Number of runs (" + nQueens + " queens)");
	p_runs.addPlot(dsp_runs);
	p_runs.setKey(JavaPlot.Key.OFF);
	p_iter.setTitle("Number of iterations (" + nQueens + " queens)");
	p_iter.addPlot(dsp_iter);
	p_iter.setKey(JavaPlot.Key.OFF);
	
	Thread thread_times = new Thread(new Runnable() {
	    public void run() {
		p_times.plot();
	    }
	});
	
	Thread thread_runs = new Thread(new Runnable() {
	    public void run() {
		p_runs.plot();
	    }
	});
	
	Thread thread_iter = new Thread(new Runnable() {
	    public void run() {
		p_iter.plot();
	    }
	});
	
	thread_times.start();
	thread_runs.start();
	thread_iter.start();
    }
    
    public static void main(String[] args) {
	Arguments arguments = new Arguments();
	CmdLineParser parser = new CmdLineParser(arguments);
	Data data = null;
	
	try {
	    parser.parseArgument(args);
	    
	    if(arguments.getPlots() == true) {
		data = new Data();
	    }
	    
	    Preprocessor preproc = new Preprocessor1();
	    SolutionGenerator generator = new Generator2(preproc);
	    
	    Neighbourhood neighbourhood = new Neighbourhood4(arguments.getTabuListSize());
	    
	    ChessQueensTS tabuSearch = new ChessQueensTS(
		    arguments.getNumberOfQueens(),
		    arguments.getTabuListSize());
	    
	    for(int i = 0 ; i < arguments.getNumberOfExecutions() ; ++i) {
		
		tabuSearch.search(neighbourhood, generator,
			arguments.getNumberOfRuns(), data,
			arguments.getVerbose());
	    }
	    
	    if(arguments.getPlots() == true) {
		drawPlots(data, arguments.getNumberOfQueens());
	    }
	}
	
	catch(CmdLineException e) {
	    System.err.println("Error : missing or wrong argument(s).");
	    System.err.println("Arguments : ");
	    parser.setUsageWidth(80);
            parser.printUsage(System.err);
	}
	
	catch(IOException e) {
	    System.err.println("Error : the writing of statistics "
		    + "in output file failed.");
	}
    }

}
