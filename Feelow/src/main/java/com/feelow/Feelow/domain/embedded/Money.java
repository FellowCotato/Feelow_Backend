package com.feelow.Feelow.domain.embedded;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;

@Embeddable
@Getter
public class Money {

    private Integer value;

    public Money(Integer value){
        this.value = value;
    }
    public Money(){this.value = 100;}

    public static Money of(Integer value){
        return new Money(value);
    }

}
