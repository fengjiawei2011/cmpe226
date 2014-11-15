public class Test {
	public static void main(String[] args) {
		int[][] m= {{1}}; 
		int target = 2;
		
		System.out.println(new Test().searchMatrix(m, target));
	}
	
	public boolean searchMatrix(int[][] matrix, int target) {
        int[] firstCol = this.getFirstCol(matrix);
        int row = this.binarySearch(target, firstCol);
        if( row < 0)
            return false;
        int col = this.binarySearch(target, matrix[row]);
        if( col >= matrix[row].length)
            return false;
        if(matrix[row][col] != target)
            return false;
        return true;
    }
    
    public int[] getFirstCol(int[][] matrix){
        int[] temp = new int[matrix.length];
        for(int i = 0; i < temp.length; i++){
            temp[i] = matrix[i][0];
        }
        
        return temp;
    }
    
    public int binarySearch(int target, int[] array){
         int  start = 0;
         int end = array.length - 1;
         while(start <= end){
             int m = (start + end)/2;
             if(target < array[m])
                end = m - 1;
             else  if(target > array[m])
                start = m + 1;
             else if( target == array[m]) 
                return m;
        
         }
         return end;
    }
}
