import java.util.Random;

public class Lottery {
	 private int[]  red = new int[6]; // 红色球
	    private int    blue;           // 蓝色球
	 
	    // 开奖
	    public void open() {
	        Random random = new Random();// 随机数生成器，在java.util包里面
	        for (int i = 0; i < this.red.length; i++) {// 生成红球
	            // random.nextInt(int);这个方法用于随机生成一个整数，范围在0-int之间
	            this.red[i] = random.nextInt(32) + 1;
	        }
	        // 蓝色球的取值范围是1-16，调用nextInt(15)会生成一个0-15直接的数，再加个1就是1-16了
	        this.blue = random.nextInt(15) + 1;
	        // 输出开奖情况
	        for (int i = 0; i < this.red.length; i++) {
	            System.out.print(this.red[i] + "\t");
	        }
	        System.out.println("\r\n" + this.blue);
	    }
	 
	    // 中奖,传入号码，匹配是否中奖，如果中奖返回中的几等奖，没总返回-1
	    public int isMiddle(int[] red,int blue) {
	        // 输出投注号码
	        for (int i = 0; i < red.length; i++) {
	            System.out.print(red[i] + "\t");
	        }
	        System.out.println("\r\n" + blue);
	        int middle = 0;// 记录中了几个球
	        if (blue == this.blue) {
	            middle += 1;// 蓝色球中
	        }
	        for (int i = 0; i < red.length; i++) {
	            if (red[i] == this.red[i]) {
	                middle += 1;// 红色球中
	            }
	        }
	        if (middle != 0) {
	            if (middle < 4) {
	                return 6;
	            }
	            else if (middle == 4) {
	                return 5;
	            }
	            else if (middle == 5) {
	                return 4;
	            }
	            else if (middle == 6) {
	                // 中6个球有两种情况，5+1和6+0，5+1是三等奖，6+0是二等奖
	                if (blue == this.blue) {
	                    return 2;// 6+0中二等奖
	                }
	                return 3;
	            }
	            else if (middle == 7) {
	                return 1;
	            }
	 
	        }
	        // 没中奖
	        return -1;
	    }
	 
	    // 测试
	    public static void main(String[] args) {
	    	Lottery pb = new Lottery();
	        pb.open();
	        System.out.println("中奖：" + pb.isMiddle(new int[] { 1, 2, 3, 4, 5, 6 }, 5));
	    }

}
