package Input;


import GUI.MainWindow;
import Schedule.AStarScheduler;
import Schedule.Algorithm;
import Schedule.BABScheduler;

public class InputHandler
{ 
	private String file;
	private String numberOfProcessors;

//	Optional argument
	private Boolean algorithm = true;
	private String numberOfCores = "1";
	private Boolean visualise = false;
	private String outputFileName = "INPUT-output";
		public InputHandler(String[] args){
            this.file = args[0];
			this.numberOfProcessors = args[1];
			String temp;
			for(int i=0; i < args.length;i++){
				temp = args[i];
				if(temp.equals("-p")){
					this.numberOfCores = args[i+1];
				}
				else if(temp.equals("-v")){
					this.visualise = true;
				}else if(temp.equals("-o")){
					this.outputFileName = args[i+1];
				}else if(temp.equals(("-a"))){
					this.algorithm = false;
				}
			}
            }

	public int getNumberOfProcessors() {
			return Integer.parseInt(this.numberOfProcessors);
	}
	
	public String getFilePath() {
		return this.file;
	}
	
	public String getOutPutName() {
		return (System.getProperty("user.dir")+"/"+this.outputFileName);
	}

	public String getNumberOfCores() {
			return this.numberOfCores;
	}

	public boolean toVisualize(){
		return this.visualise;
	}

	public Algorithm getAlgorithm(){
			Algorithm defau = new AStarScheduler(TaskSchedule.getInstance().getGraph(),this);
			Algorithm BAB = new BABScheduler(TaskSchedule.getInstance().getGraph(),this);
			Algorithm finalAlgorithm = this.algorithm? defau:BAB;
			return finalAlgorithm;
	}

	public void setAlgorithm(Boolean isAstar){
			this.algorithm = isAstar;
	}
}
