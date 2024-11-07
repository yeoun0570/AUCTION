package auction.auction_back.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "user_id")
    private String userId;

    private String nickname;
    private String email;

    private int balance;

    @Builder
    public User(String nickname, String email, int balance) {
        this.nickname = nickname;
        this.email = email;
        this.balance = balance;
    }

    //자금 추가
    public void addBalance(int amount) {
        this.balance += amount;
    }

    //자금 차감
    public void subtractBalance(int amount) {
        if(this.balance < amount) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        this.balance -= amount;
    }

}
