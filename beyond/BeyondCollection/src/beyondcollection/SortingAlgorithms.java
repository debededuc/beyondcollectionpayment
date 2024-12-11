
package beyondcollection;

import java.util.*;
import java.sql.*; 
public class SortingAlgorithms {

    public static void quickSort(List<Feedback> feedbackList, int low, int high) {
        if (low < high) {
            int pi = partition(feedbackList, low, high);
            quickSort(feedbackList, low, pi - 1);
            quickSort(feedbackList, pi + 1, high);
        }
    }

    private static int partition(List<Feedback> feedbackList, int low, int high) {
        Feedback pivot = feedbackList.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (feedbackList.get(j).getRating() < pivot.getRating()) {
                i++;
                Collections.swap(feedbackList, i, j);
            }
        }
        Collections.swap(feedbackList, i + 1, high);
        return i + 1;
    }

    public static List<Feedback> sortFeedbackByPurposeAndRating(List<Feedback> feedbackList) {
        feedbackList.sort((f1, f2) -> {
            
            int purposeComparison = f1.getPurpose().compareTo(f2.getPurpose());
            
            
            if (purposeComparison == 0) {
                return Integer.compare(f2.getRating(), f1.getRating());  
            }
            return purposeComparison; 
        });
        return feedbackList;
    }
    
    
    public static void mergeSort(List<OrderReturn> orderReturns, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(orderReturns, left, mid);
            mergeSort(orderReturns, mid + 1, right);
            merge(orderReturns, left, mid, right);
        }
    }

    private static void merge(List<OrderReturn> orderReturns, int left, int mid, int right) {
        List<OrderReturn> leftList = new ArrayList<>(orderReturns.subList(left, mid + 1));
        List<OrderReturn> rightList = new ArrayList<>(orderReturns.subList(mid + 1, right + 1));

        int i = 0, j = 0, k = left;
        while (i < leftList.size() && j < rightList.size()) {
            
            if (leftList.get(i).getDateRequested().before(rightList.get(j).getDateRequested())) {
                orderReturns.set(k++, leftList.get(i++));
            } else {
                orderReturns.set(k++, rightList.get(j++));
            }
        }

        
        while (i < leftList.size()) {
            orderReturns.set(k++, leftList.get(i++));
        }
        while (j < rightList.size()) {
            orderReturns.set(k++, rightList.get(j++));
        }
    }


   
    public static void heapSort(List<Refund> refunds) {
        int n = refunds.size();
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(refunds, n, i);
        }
        for (int i = n - 1; i >= 0; i--) {
            Collections.swap(refunds, 0, i);
            heapify(refunds, i, 0);
        }
    }

    private static void heapify(List<Refund> refunds, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && refunds.get(left).getPriority() > refunds.get(largest).getPriority()) {
            largest = left;
        }
        if (right < n && refunds.get(right).getPriority() > refunds.get(largest).getPriority()) {
            largest = right;
        }

        if (largest != i) {
            Collections.swap(refunds, i, largest);
            heapify(refunds, n, largest);
        }
    }
}
