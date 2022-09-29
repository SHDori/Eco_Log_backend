package Eco_Log.Eco_Log.domain.user;

import Eco_Log.Eco_Log.domain.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter

@Table(name = "user_summary")
public class Summary extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "Summary_id")
    private Long id;

    @OneToOne(mappedBy = "summary",fetch = FetchType.LAZY)
    private Users user;

    private int countOfConsumption;
    private int countOfOutside;
    private int countOfLiving;
    private int countOfEat;
    private int countOfOthers;


    public Summary(){
        this.countOfConsumption=0;
        this.countOfOutside=0;
        this.countOfLiving=0;
        this.countOfEat=0;
        this.countOfOthers=0;
    }

    public void plusCountOfConsumption(){
        this.countOfConsumption+=1;
    }

    public void plusCountOfOutSide(){
        this.countOfOutside+=1;
    }

    public void plusCountOfLiving(){
        this.countOfLiving+=1;
    }

    public void plusCountOfEat(){
        this.countOfEat+=1;
    }

    public void plusCountOfOthers(){
        this.countOfOthers+=1;
    }
/////////////////////////////
    public void minusCountOfConsumption(){
        this.countOfConsumption-=1;
    }

    public void minusCountOfOutSide(){
        this.countOfOutside-=1;
    }

    public void minusCountOfLiving(){
        this.countOfLiving-=1;
    }

    public void minusCountOfEat(){
        this.countOfEat-=1;
    }

    public void minusCountOfOthers(){
        this.countOfOthers-=1;
    }


    @Override
    public String toString() {
        return "Summary{" +
                "id=" + id +
                ", user=" + user.getName() +
                ", 소비=" + countOfConsumption +
                ", 외출=" + countOfOutside +
                ", 생활=" + countOfLiving +
                ", 식사=" + countOfEat +
                ", 기타=" + countOfOthers +
                '}';
    }
}
