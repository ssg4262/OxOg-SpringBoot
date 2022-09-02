package com.Oxog.Beta.utilModel;

import com.Oxog.Beta.model.User;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Entity
@Table(name = "heart")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Heart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "campaign_id")
    @NonNull
    private String campaignId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}