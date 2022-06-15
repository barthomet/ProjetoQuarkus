//criação do crud

package io.github.barthomet.quarkus.rest;

import io.github.barthomet.quarkus.domain.model.User;
import io.github.barthomet.quarkus.domain.repository.UserRepository;
import io.github.barthomet.quarkus.rest.dto.CreateUserRequest;
import io.github.barthomet.quarkus.rest.dto.ResponseError;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;


@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserRepository repository;
    private Validator validator;

    @Inject
    public UserResource(UserRepository repository, Validator validator){
        this.repository = repository;
        this.validator = validator;
    }

    @POST
    @Transactional
    public Response createUser(CreateUserRequest userRequest){

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(userRequest);
        if (!violations.isEmpty()){
            ResponseError responseError = ResponseError.createFromValidation(violations);
            return Response.status(422).entity(responseError).build();
        }

        User user = new User();
        user.setNome(userRequest.getNome());
        user.setEmail(userRequest.getEmail());

        repository.persist(user);

        return Response.status(Response.Status.CREATED.getStatusCode()).entity(user).build();

    }

    @GET
    public Response listAllUsers(){
        PanacheQuery<User> query = repository.findAll();
        return Response.ok(query.list()).build();

    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response updateUser(@PathParam("id") Long id, CreateUserRequest userData){
        User user = repository.findById(id);

        if (user != null) {
            user.setNome(userData.getNome());
            user.setEmail(userData.getEmail());

            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();

    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response deleteUser(@PathParam("id") Long id){
        User user = repository.findById(id);
        if (user != null){
            repository.delete(user);
            return Response.ok().build();
        }
         return Response.status(Response.Status.NOT_FOUND).build();
    }
}
