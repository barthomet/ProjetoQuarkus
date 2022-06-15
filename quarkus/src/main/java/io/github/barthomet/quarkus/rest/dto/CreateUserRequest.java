//criação do tipo usuario
package io.github.barthomet.quarkus.rest.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    @NotBlank(message = "Email é obrigatório")
    private String email;
}
