package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Account {
    private String cardId;
    private String password;
    private int balance;

    public Account(){
        balance = 0;
        cardId = generateCardId();
        password = generatePIN();
    }

    public Account(String cardId, String PIN, int balance){
        this.cardId = cardId;
        this.password = PIN;
        this.balance = balance;
    }

    public String getCardId() {
        return cardId;
    }

    public String getPassword() {
        return password;
    }

    public int getBalance() {
        return balance;
    }

    public boolean updateBalance(int amount) {
        int result = balance + amount;

        if(result < 0)
            return false;

        balance = result;
        return true;
    }

    public String generateCardId() {
        Random random = new Random();
        List<Integer> nums = new ArrayList<>();

        // Add initial fixed 6 digit of numberss
        nums.add(4);
        nums.add(0);
        nums.add(0);
        nums.add(0);
        nums.add(0);
        nums.add(0);

        String cardId = "400000";

        for (int i=1; i<10; i++){
            int num = random.nextInt(10);
            cardId += num;
            nums.add(num);
        }

        cardId += LuhnAlgorithm(nums);

        return cardId;
    }

    public String generatePIN() {
        Random random = new Random();

        String PIN = "";

        for (int i=1; i<=4; i++){
            PIN += random.nextInt(10);
        }
        return PIN;
    }

    public static int LuhnAlgorithm(List<Integer> nums) {
        int sum = 0;
        for (int i=0; i<nums.size(); i++){
            int num = nums.get(i);
            if(i%2==0)
                num = num*2;
            if(num>9)
                num -= 9;
            sum+=num;
        }

        return (int)(Math.ceil(sum/10.0) * 10.0 - (double)sum);
    }
}
