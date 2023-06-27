import parcs.*;

public class MatrixFrobeniusNormTask implements AM {

    public void run(AMInfo info) {
        // отримання даних з каналу
        double[][] data = (double[][]) info.parent.readObject();

        // Обчислення норми Фробеніуса
        double norm = 0.0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                norm += Math.pow(data[i][j], 2);
            }
        }

        // передача результату на батьківський вузол
        info.parent.write((Double)norm);
    }
}