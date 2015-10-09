import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;


/**
 * Program to quantize the vehicle speeds into bins and perform
 * one dimensional clustering using Otsu's method to calculate
 * the best threshold and mixed variance
 * 
 * @author ajinkyarode
 *
 */
public class HW02_Rode_Ajinkya_program {

	/**
	 * Method to calculate the mean
	 * 
	 * @param m
	 * 		Input array
	 * @return
	 * 		Mean
	 */
	public static double mean(double[] m) {
		double sum = 0;
		for (int i = 0; i < m.length; i++) {
			sum += m[i];
		}
		return sum / m.length;
	}

	/**
	 * Method to calculate the median
	 * 
	 * @param m
	 * 		Input array
	 * @return
	 * 		Median
	 */
	public static double median(double[] m) {
		int mid = m.length/2;
		if (m.length % 2 == 1) {
			return m[mid];
		} else {
			return (m[mid-1] + m[mid]) / 2.0;
		}
	}

	/**
	 * Method to calculate the mode
	 * 
	 * @param a
	 * 		Input array
	 * @return
	 * 		Mode (Here max value as we have created bins)
	 */
	public static double mode(int a[]) {
		double max = a[0];

		for (int i = 1; i < a.length; i++) {
			if (a[i] > max) {
				max = a[i];
			}
		}
		return max;
	}

	/**
	 * 	Method to find the threshold at which the speeds 
	 * 	are divided into two clusters 
	 * 
	 * @param binFreq
	 * 		The array which contains frequency of each bin
	 * @return
	 * 		threshold value
	 */
	public static double otsu(int[] binFreq)
	{	
		double mixed_var[]=new double[21];
		double Rho_l[]=new double[21];
		double Rho_r[]=new double[21];
		double W_l[]=new double[21]; 
		double W_r[]=new double[21]; 
		double n=128;
		double temp1=0;
		double temp3;
		double temp2;
		double temp4;
		double min=0;
		double mid_mean1; 
		double mid_mean2; 
		double best_mixed_var=1000;
		double best_threshold=0;
		for(int i=0;i<21;i++)
		{
			temp2=0;
			mid_mean1=0;
			temp1=temp1+binFreq[i];
			for(int j=0;j<=i;j++)
			{
				temp2=temp2+binFreq[j];
			}

			// Fraction of data to the left of threshold

			W_l[i]=temp2/n;

			// Fraction of data to the right of threshold

			W_r[i]=(n-W_l[i])/n;

			temp3=0;
			for(int k=0;k<=i;k++)
			{
				temp3=temp3+binFreq[k];
			}
			mid_mean1=temp3/(i+1);

			// Variance of data to the left of threshold

			Rho_l[i]=Math.pow(mid_mean1-temp3,2)/(i+1);

			temp4=0;
			for(int l=20;l>i+1;l--)
			{
				temp4=temp4+binFreq[l];
			}
			mid_mean2=temp4/(21-i+1);

			// Variance of data to the right of threshold

			Rho_r[i]=Math.pow(mid_mean2-temp4,2)/(21-i+1);

			// Mixed variance calculated using --> W-L*RHO-L + W-R*RHO-R

			mixed_var[i]=(W_l[i]*Rho_l[i])+(W_r[i]*Rho_r[i]);
		}
		
		// Calculate the best mixed variance and threshold
		
		for(int i=1;i<22;i++)
		{
			if (mixed_var[i-1]<best_mixed_var)
			{
				best_mixed_var = mixed_var[i-1];
				best_threshold = i;
			}
		}
		System.out.println("\n****Results****");
		System.out.println("\nBest Threshold: "+best_threshold);
		Arrays.sort(mixed_var);
		min=mean(mixed_var);
		double best_var=min/10;
		return best_var;
	}

	/**
	 * Method to read the Comma Separated Value (.csv) file 
	 * from the user and store the values in an array
	 * 
	 * @param file_name
	 * 		Name of file to be read
	 * @return
	 * 		Array of extracted values
	 */
	public static double[] readFile(String file_name)
	{
		BufferedReader fileReader = null;
		final String DELIMITER = ",";
		double inputs[]=new double[128];
		try
		{
			String line = "";
			fileReader = new BufferedReader(new FileReader(file_name));
			int p=0;
			while ((line = fileReader.readLine()) != null)
			{
				String[] tokens = line.split(DELIMITER);
				for(String token : tokens)
				{
					inputs[p]=Double.parseDouble(token);
					p++;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return inputs;
	}

	/**
	 * Method to quantize the input into bins of size 2 and 
	 * store the frequencies of the nearest values into single bin
	 * 
	 * @param inputs
	 * 		Take input as an array of all possible speeds
	 * @param start
	 * 		Starting point of the bin
	 * @param end
	 * 		Ending point of the bin
	 * @return
	 * 		Quantized array into bins of size 2
	 */
	public static int[] quantize(double[] inputs,int start,int end)
	{
		int binFreq[]=new int[21];
		end=end+1;
		int count=0;
		for(int i=start; i<=end; i=i+2){
			for(int j=0; j<inputs.length; j++){
				if(inputs[j]>i&&inputs[j]<=i+2)
				{
					binFreq[count]++;
				}	
			}
			count++; 
		}
		return binFreq;
	}

	/**
	 * Main method 
	 * 
	 * @param args
	 */
	public static void main(String args[])
	{
		String fileToParse1 = "UNCLASSIFIED_Speed_Observations_for_128_vehicles.csv";
		int start=38;
		int end=80; 
		double mean;
		double median;
		double mode;
		double best_var;
		double inputs[]=readFile(fileToParse1);
		int binFreq[]=quantize(inputs,38,80);    
		System.out.print("Bin frequency:\n");
		System.out.print("[");
		int binNum[]=new int[22];
		int t=0;
		for(int i=start; i<=end; i=i+2){
			binNum[t]=i;
			t++;
		}
		
		// Displaying the Bin Number along with the frequency
		
		for(int k=0;k<binFreq.length;k++)
		{
			System.out.println(binNum[k]+ " -> "+ binFreq[k]);
		}
		System.out.print("]\n");
		Arrays.sort(inputs);
		mode=mode(binFreq);
		mean=mean(inputs);
		median=median(inputs);
		best_var=otsu(binFreq);
		System.out.println("Mean: " +mean);
		System.out.println("Median: " +median);
		System.out.println("Mode: " +mode);
		System.out.println("Mixed Variance: " +best_var);

		/****PART TWO (for reference)****/ 

		//String fileToParse2 = "Mystery_Data.csv";
		//double inputs1[]=readFile1(fileToParse2);
		//double mean1;
		//double median1;
		//double mode1;
		//mode1=max(inputs1);
		//mean1=mean(inputs1);
		//Arrays.sort(inputs1);
		//median1=median(inputs1);
		//System.out.println("\nMean: " +mean1);
		//System.out.println("Median: " +median1);
		//System.out.println("Mode: " +mode1);
	}
}
