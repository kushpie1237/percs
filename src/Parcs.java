import java.io.*;
import java.util.*;
import java.util.Random;
import parcs.*;


public class Parcs implements AM
{

    public static void main(String[] args)
    {
        System.out.println("The Solver class start method main");

        task curtask = new task();
        curtask.addJarFile("Parks.jar");
        curtask.addJarFile("MatrixFrobeniusNormTask.jar");

        System.out.println("The Solver class method main adder jar files");

        (new Parcs()).run(new AMInfo(curtask, (channel)null));

        System.out.println("The Solver class method main finish work");
        curtask.end();
    }

    public void run(AMInfo info)
    {
        int m, n, deamons;

        try
        {
            BufferedReader in = new BufferedReader(new FileReader(info.curtask.findFile("input_1.txt")));

            m = Integer.parseInt(in.readLine());
            n = Integer.parseInt(in.readLine());
            deamons = Integer.parseInt(in.readLine());
        }
        catch (IOException e)
        {
            System.out.print("Reading input data error\n");
            e.printStackTrace();
            return;
        }

        Random random = new Random();
        int maxValue = 100;
        // створення матриці та заповнення її випадковими числами
        int[][] matrix = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = random.nextInt(maxValue);
            }
        }

        System.out.println("The Solver class have read data from the parent server");
        System.out.println("m = " + m);
        System.out.println("n = " + n);
        // Time counting
        long tStart = System.nanoTime();

        long res = solve(info, deamons,  m, n, matrix);

        long tEnd = System.nanoTime();

        System.out.println("Working time on matrix" + m + "x" + n + " processes: " + ((tEnd - tStart) / 1000000) + "ms");
        System.out.println("Frobenius norm = " + res);
    }

    static public long solve(AMInfo info, int deamons, int m, int n, int[][] a)
    {
        long Res = 0;

        List <point> points = new ArrayList<point>();
        List <channel> channels = new ArrayList<channel>();
        // Connection to points
        for (int versionNumber = 0; versionNumber < deamons; versionNumber++) {
            int x = versionNumber*n/deamons;
            int y = (versionNumber+1)*n/deamons - 1;
            double [][] matrix = new double[m][y+1];

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n/deamons; j++) {
                   matrix [i][j] = a[i][x + j];
                }
            }
            System.out.println(versionNumber);
            points.add(info.createPoint());
            System.out.println(points);
            channels.add(points.get(versionNumber).createChannel());
            points.get(versionNumber).execute("MatrixFrobeniusNormAlgorithm");
            channels.get(versionNumber).write(matrix);
        }

        // Mapping results
        for(int versionNumber = 0; versionNumber < deamons; versionNumber++)
        {
            Res+=channels.get(versionNumber).readLong();
        }
        double Result = 0;
        Result = Math.sqrt(Res);

        return (long) Result;
    }
}