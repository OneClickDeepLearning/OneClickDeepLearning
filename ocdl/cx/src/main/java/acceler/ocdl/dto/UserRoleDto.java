package acceler.ocdl.dto;

import acceler.ocdl.entity.Role;
import acceler.ocdl.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDto {

    private User user;

    private Role role;

}
