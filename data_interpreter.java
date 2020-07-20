import java.io.*;
import java.util.Scanner;
import java.util.*;
import java.time.*;
import java.net.*;
import java.math.*;


public class data_interpreter {
	
	public static void main (String[] args) {
		LocalDate today = LocalDate.now();
		String todaysDate = today.toString();
		URL csvURL;
		Scanner scnr = new Scanner(System.in);
		System.out.println();
		System.out.println("Todays Date is " + todaysDate);
		System.out.println("Please wait as todays data is retrieved from the web... ");
		int choice = 0;

		try {
			URL url = new URL("https://covid.ourworldindata.org/data/owid-covid-data.csv");
			URLConnection connection = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			boolean stillRunning = true; 
			String line; 
			ArrayList<String> file  = new ArrayList<String>();
			
			//Grab the data from the csv file online
			while ((line = in.readLine()) != null) {
				file.add(line);
			}
			
			/*=== MAIN MENU ===*/
			while (stillRunning) {
				System.out.println();
				System.out.println("===== Main Menu =====" + "\n");
				System.out.println("(Please enter a number and press enter)");				
				System.out.println("(1): Print all regions total cases.");
				System.out.println("(2): Print the data for the region with the most cases.");
				System.out.println("(3): Print correlational coefficients.");
				System.out.println("(4): Quit.");
				if (scnr.hasNextInt())
					choice = scnr.nextInt();
				else {
					choice = 0;
				}
				if (choice > 0 && choice < 5) {
					if(choice == 1) {
						print_all_cases(file, todaysDate);
					}
					else if(choice == 2) {
						get_most_cases_data(file, todaysDate);
					}
					else if(choice == 3) {
						print_correlational_data(file, todaysDate);
					}
					else if(choice == 4) {
						stillRunning = false;
					}
				}
				else {
					System.out.println("Please enter a valid choice.");
					scnr.nextLine();
				}
			}
			in.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void get_most_cases_data(ArrayList<String> file, String todaysDate) {
		String line; 
		String splitBy = ",";
		double most_cases = 0.0;
		String new_cases = "";
		String new_deaths = "";
		String total_deaths = "";
		String region = "";
		String total_tests = "";
		String total_tests_per_thousand = "";
		String population_density = "";
		String median_age = "";
		String gdp_per_capita = "";
		String extreme_poverty = "";
		
		//Get the data for the region with the most reported cases
		for(int i = 1; i < file.size(); i++) {
			line = file.get(i);
			String[] entry = line.split(splitBy);
			if((entry[4].length() > 0) && !entry[2].equals("World")) {
				if(Double.parseDouble(entry[4]) >= most_cases) {
					region = entry[2];
					most_cases = Double.parseDouble(entry[4]);
					new_cases = entry[5];
					total_deaths = entry[6];
					new_deaths = entry[7];
					total_tests = entry[13];
					total_tests_per_thousand = entry[15];
					population_density = entry[21];
					median_age = entry[22];
					gdp_per_capita = entry[25];
					extreme_poverty = entry[26];
				}
			}
		}
		System.out.println();
		System.out.print(region + " is the region with the most cases to date, there are: ");
		System.out.println(most_cases + " total cases to date. \n");
		System.out.println(new_cases + " new cases were reported yesterday \n");
		System.out.println("There have been " + total_deaths + " total deaths. \n");
		System.out.println("There were " + new_deaths + " new deaths reported yesterday \n");
		System.out.println("Total tests performed in this region are: " + total_tests + "\n");
		System.out.println("Tests per thousand people: " + total_tests_per_thousand + "\n");
		System.out.println("Population density(# of people per 1 square mile) is: " + population_density + "\n");
		System.out.println("The median age in this region is:" + median_age + "\n");
		System.out.println("The gdp per capita(Regions GDP / Population) is: " + gdp_per_capita + "\n");
		System.out.println("Extreme poverty rate in the region(Living on less than \n"); 
		System.out.println("$1.25 a day) is: " + extreme_poverty + "\n");
	}
	
	public static void print_correlational_data(ArrayList<String> file, String todaysDate) {
		String line; 
		String splitBy = ",";
		
		/*==== Relational Coefficients ====*/
		float diabetes_r = 0;
		float gdp_r = 0;
		float handwashing_facilities_r = 0;
		float hospital_beds_r = 0;
		float smokers_r = 0;
		float median_age_r = 0;
		
		/* ==== Data points for pearsons correlation formula ====*/
		ArrayList<Float> age_nums = new ArrayList<Float>(); //22
		ArrayList<Float> age_nums_dr = new ArrayList<Float>();
		ArrayList<Float> gdp_nums = new ArrayList<Float>(); //25
		ArrayList<Float> gdp_nums_dr = new ArrayList<Float>();
		ArrayList<Float> diabetes_nums = new ArrayList<Float>(); //28
		ArrayList<Float> diabetes_nums_dr = new ArrayList<Float>();
		ArrayList<Float> smokers_nums = new ArrayList<Float>(); //29 + 30
		ArrayList<Float> smokers_nums_dr = new ArrayList<Float>();
		ArrayList<Float> handwashing_nums = new ArrayList<Float>(); //31
		ArrayList<Float> handwashing_nums_dr = new ArrayList<Float>();
		ArrayList<Float> hospital_beds_nums = new ArrayList<Float>(); //32
		ArrayList<Float> hospital_beds_nums_dr = new ArrayList<Float>();
		
		
		for(int i = 1; i < file.size(); i++) {
			line = file.get(i);
			String[] entry = line.split(splitBy);
			
			if (entry.length >= 33) {
				if(entry[22].length() > 0  &&  entry[27].length() > 0 
				&& entry[3].equals(todaysDate)) {
					age_nums.add(Float.parseFloat(entry[22]));
					age_nums_dr.add(Float.parseFloat(entry[20]));
				}
				
				if(entry[25].length() > 0 && entry[27].length() > 0 
				&& entry[3].equals(todaysDate)) {
					gdp_nums.add(Float.parseFloat(entry[25]));
					gdp_nums_dr.add(Float.parseFloat(entry[27]));
				}
				if(entry[28].length() > 0 && entry[27].length() > 0 
				&& entry[3].equals(todaysDate)) {
					diabetes_nums.add(Float.parseFloat(entry[28]));
					diabetes_nums_dr.add(Float.parseFloat(entry[27]));
				}
				if(entry[29].length() > 0 && entry[30].length() > 0 
				&& entry[27].length() > 0 && entry[3].equals(todaysDate)) {
					smokers_nums.add(Float.parseFloat(entry[29]) + Float.parseFloat(entry[30]));
					smokers_nums_dr.add(Float.parseFloat(entry[27]));
				}
				if(entry[31].length() > 0 && entry[27].length() > 0 
				&& entry[3].equals(todaysDate)) {
					handwashing_nums.add(Float.parseFloat(entry[31]));
					handwashing_nums_dr.add(Float.parseFloat(entry[27]));
				}
				if(entry[32].length() > 0 && entry[27].length() > 0 
				&& entry[3].equals(todaysDate)) {
					hospital_beds_nums.add(Float.parseFloat(entry[32]));
					hospital_beds_nums_dr.add(Float.parseFloat(entry[27]));
				}
			}
		}
		/*==== Set the correlational coefficients ====*/
		median_age_r = pearsons_coorelation(age_nums, age_nums_dr, age_nums.size());
		gdp_r = pearsons_coorelation(gdp_nums, gdp_nums_dr, gdp_nums.size());
		diabetes_r = pearsons_coorelation(diabetes_nums, diabetes_nums_dr, diabetes_nums.size());
		smokers_r = pearsons_coorelation(smokers_nums, smokers_nums_dr, smokers_nums.size());
		handwashing_facilities_r = pearsons_coorelation(handwashing_nums, handwashing_nums_dr, handwashing_nums.size());
		hospital_beds_r = pearsons_coorelation(hospital_beds_nums, hospital_beds_nums_dr, hospital_beds_nums.size());
		
		/*==== Print the results ====*/
		System.out.println("Relational Coefficient for the median age and covid-19 death rate: " + median_age_r);
		System.out.println("Relational Coefficient for diabetes prevalence and covid-19 death rate: " + diabetes_r);
		System.out.println("Relational Coefficient for gdp per capita and covid-19 death rate: " + gdp_r);
		System.out.println("Relational Coefficient for prevalence of handwashing facilities and covid-19 death rate: " + handwashing_facilities_r);
		System.out.println("Relational Coefficient for number of hospital beds per thousand and covid-19 death rate: " + hospital_beds_r);
		System.out.print("Relational Coefficient for number of smokers (assuming 50% male and 50% female population"); 
		System.out.println("and covid-19 death rate: " + smokers_r);
	}
	
	public static void print_all_cases(ArrayList<String> file, String todaysDate) {
		String line;
		String splitBy = ",";
		boolean gotTodaysData = false;
		for(int i = 0; i < file.size(); i++) {
			line = file.get(i);
			String[] entry = line.split(splitBy);
			if(entry[3].equals(todaysDate)){
				System.out.println("Total Cases for " + entry[2] + ": " + entry[4]);
				gotTodaysData = true;
			}
		}
		if (!gotTodaysData) {
			System.out.println("Todays data is not yet available. Please try again later on.");
		}
	}
	
	//Returns pearsons correlational coefficient as a float
	public static float pearsons_coorelation(ArrayList<Float> X, ArrayList<Float> Y, int n) {
		float sum_xy = 0;
		float sum_x = 0;
		float sum_y = 0;
		float sum_x_squared = 0;
		float sum_y_squared = 0; 
		float r = 0;
		
		for(int i = 0; i < n ; i++) {
		sum_xy = sum_xy + X.get(i) * Y.get(i);
		sum_x = sum_x + X.get(i);
		sum_y = sum_y + Y.get(i);
		sum_x_squared = sum_x_squared + (X.get(i) * X.get(i));
		sum_y_squared = sum_y_squared + (Y.get(i) * Y.get(i));
		}
		
		r = (float)(n * (sum_xy) - (sum_x * sum_y)) /
		(float)(Math.sqrt( (n * sum_x_squared - 
		sum_x * sum_x) * (n * sum_y_squared - sum_y * sum_y) ) );
		return r; 
	}
}























