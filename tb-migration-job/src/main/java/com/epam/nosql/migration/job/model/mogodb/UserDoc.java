package com.epam.nosql.migration.job.model.mogodb;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class UserDoc {
    @Id
    private String id;
    private String name;
    private String email;
    private List<String> ticketIds;
    private BigDecimal money;

    public UserDoc(String name, String email, List<String> ticketIds, BigDecimal money) {
        this.name = name;
        this.email = email;
        this.ticketIds = ticketIds;
        this.money = money;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof UserDoc userDoc)) return false;

        return Objects.equals(id, userDoc.id) && Objects.equals(name, userDoc.name) && Objects.equals(email, userDoc.email) && Objects.equals(ticketIds, userDoc.ticketIds) && Objects.equals(money, userDoc.money);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(ticketIds);
        result = 31 * result + Objects.hashCode(money);
        return result;
    }
}
