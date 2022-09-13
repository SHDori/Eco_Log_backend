package Eco_Log.Eco_Log.domain.user.randomNickName;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RandomNicknamaMaker {


    private List<String> randomFirstName = Arrays.asList("앞서가는 ", "선행하는 ", "활기찬 ","열정적인 ", "에코에코한 ",
            "긍정적인 ","자연친화적인 ","푸릇푸릇한 ");

    private List<String> randomLastName = Arrays.asList("완두콩","비기너","활동가","에코피플");



    public String getRandomNickName(){
        Collections.shuffle(randomFirstName);
        Collections.shuffle(randomLastName);
        return randomFirstName.get(0)+randomLastName.get(0);
    }


}
