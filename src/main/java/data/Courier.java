package data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Courier {
    @NonNull
    private String login;
    @NonNull
    private String password;
    private String firstName;
}
