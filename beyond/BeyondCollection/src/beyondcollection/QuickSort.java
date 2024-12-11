package beyondcollection;


import beyondcollection.Order;

public class QuickSort {

    public static void quickSort(Order[] orders, int low, int high) {
        if (low < high) {
            int pi = partition(orders, low, high);
            quickSort(orders, low, pi - 1);  // Before pi
            quickSort(orders, pi + 1, high); // After pi
        }
    }

    private static int partition(Order[] orders, int low, int high) {
        // Use the last element as the pivot
        java.sql.Date pivot = orders[high].getEstimatedArrivalDate(); // pivot
        int i = (low - 1); // Index of smaller element

        for (int j = low; j < high; j++) {
            java.sql.Date currentDate = orders[j].getEstimatedArrivalDate();
            // Check if currentDate is null
            if (currentDate == null) {
                // If currentDate is null, we can choose to place it at the end
                // or handle it differently based on your requirements
                continue; // Skip null values
            }

            // If current element is smaller than or equal to pivot
            if (pivot != null && currentDate.compareTo(pivot) <= 0) {
                i++;
                // swap orders[i] and orders[j]
                Order temp = orders[i];
                orders[i] = orders[j];
                orders[j] = temp;
            }
        }

        // swap orders[i + 1] and orders[high] (or pivot)
        Order temp = orders[i + 1];
        orders[i + 1] = orders[high];
        orders[high] = temp;

        return i + 1;
    }
}