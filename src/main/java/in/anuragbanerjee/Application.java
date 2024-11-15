package in.anuragbanerjee;

import in.anuragbanerjee.analyzer.FlowLogAnalyzer;
import in.anuragbanerjee.mapper.TagMapper;
import in.anuragbanerjee.output.OutputGenerator;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        // Initialize components
        TagMapper tagMapper = new TagMapper();
        OutputGenerator outputGenerator = new OutputGenerator();
        FlowLogAnalyzer analyzer = new FlowLogAnalyzer("default", tagMapper, outputGenerator);

        // Default paths
        String projectRoot = System.getProperty("user.dir");
        String defaultFlowLogsPath = projectRoot + "/src/main/resources/flowlogs.txt";
        String defaultLookupPath = projectRoot + "/src/main/resources/lookup.txt";
        String defaultOutputPath = projectRoot + "/src/main/resources/output.txt";

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose an option for file paths:");
        System.out.println("1. Enter custom file paths");
        System.out.println("2. Use default file paths");
        System.out.print("Enter your choice (1 or 2): ");

        String choice = scanner.nextLine().trim();

        String flowLogsPath, lookupPath, outputPath;

        switch (choice) {
            case "1":
                System.out.println("\nEnter custom file paths:");
                System.out.print("Enter flow logs file path: ");
                flowLogsPath = scanner.nextLine().trim();

                System.out.print("Enter lookup file path: ");
                lookupPath = scanner.nextLine().trim();

                System.out.print("Enter output file path: ");
                outputPath = scanner.nextLine().trim();
                break;

            case "2":
                System.out.println("\nUsing default file paths:");
                System.out.println("Flow logs path: " + defaultFlowLogsPath);
                System.out.println("Lookup path: " + defaultLookupPath);
                System.out.println("Output path: " + defaultOutputPath);

                flowLogsPath = defaultFlowLogsPath;
                lookupPath = defaultLookupPath;
                outputPath = defaultOutputPath;
                break;

            default:
                System.out.println("Invalid choice. Using default paths.");
                flowLogsPath = defaultFlowLogsPath;
                lookupPath = defaultLookupPath;
                outputPath = defaultOutputPath;
                break;
        }

        // Analyze logs
        try {
            analyzer.analyze(flowLogsPath, lookupPath, outputPath);
            System.out.println("\nAnalysis completed successfully!");
            System.out.println("Output file generated at: " + outputPath);
        } catch (Exception e) {
            System.err.println("\nError during analysis: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
