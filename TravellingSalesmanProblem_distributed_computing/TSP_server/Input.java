import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Input {
	int numCities = 0;
	int[][] weights;
	public int[][] readFromFile(File file) throws IOException, NumberFormatException {
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        Integer[] xCoord = null, yCoord = null;
        while ((line = bufferedReader.readLine()) != null) {
            String[] stringArray = line.split(" ");
            if (stringArray.length > 0) {
                if ("DIMENSION".equals(stringArray[0])) {
                    this.numCities = Integer.parseInt(stringArray[2]);
                    xCoord = new Integer[this.numCities];
                    yCoord = new Integer[this.numCities];
                }
                else if (stringArray[0].matches("\\d+")) {
                    int point = Integer.parseInt(stringArray[0]) - 1;
                    xCoord[point] = Integer.parseInt(stringArray[1]);
                    yCoord[point] = Integer.parseInt(stringArray[2]);
                }
            }
        }
        bufferedReader.close();

        // adjacency matrix describing the distance between the cities
        this.weights = new int[this.numCities][this.numCities];
        for (int u = 0; u < this.numCities; ++u) {
            this.weights[u][u] = 0;
            for (int v = u + 1; v < this.numCities; ++v) {
                long xDist = xCoord[u] - xCoord[v];
                long yDist = yCoord[u] - yCoord[v];
                this.weights[u][v] = (int)Math.sqrt(xDist * xDist + yDist * yDist);
                this.weights[v][u] = this.weights[u][v];
            }
        }
        return weights;
}
}
