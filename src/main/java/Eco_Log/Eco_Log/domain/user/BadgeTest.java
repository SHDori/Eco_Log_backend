package Eco_Log.Eco_Log.domain.user;


import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable // jpa의 내장타입이라는것을 명시
@Getter
public class BadgeTest {

    private boolean badge_1;
    private boolean badge_2;
    private boolean badge_3;
    private boolean badge_4;
}
