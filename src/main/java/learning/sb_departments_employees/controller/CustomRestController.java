package learning.sb_departments_employees.controller;

import learning.sb_departments_employees.model.Employee;
import learning.sb_departments_employees.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.webmvc.HttpHeadersPreparer;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.rest.webmvc.support.ETag;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@RepositoryRestController
public class CustomRestController {
    private final EmployeeRepository empRepository;
    private final HttpHeadersPreparer headersPreparer;
    private final Repositories repositories;

    @Autowired
    public CustomRestController(EmployeeRepository empRepository, HttpHeadersPreparer headersPreparer,
                                Repositories repositories) {
        this.empRepository = empRepository;
        this.headersPreparer = headersPreparer;
        this.repositories = repositories;
    }

//    @GetMapping(path = "/employees/{empName}")  // names should match if no explicit mapping
    public ResponseEntity<?> getEmployee(@PathVariable String empName, @RequestHeader HttpHeaders headers,
                                         PersistentEntityResourceAssembler assembler)
                             throws ResourceNotFoundException {
        // Default handler: org.springframework.data.rest.webmvc.RepositoryEntityController.getItemResource
        // The code below is adapted copy of the default handler
        var data = empRepository.findByName(empName).orElseThrow(ResourceNotFoundException::new);

        // >>> Adapted copy from org.springframework.data.rest.webmvc.ResourceStatus.getStatusAndHeaders
        // Check ETag for If-Non-Match
        List<String> ifNoneMatch = headers.getIfNoneMatch();
        ETag eTag = ifNoneMatch.isEmpty() ? ETag.NO_ETAG : ETag.from(ifNoneMatch.get(0));
        var persistentEntity = repositories.getPersistentEntity(Employee.class);
        HttpHeaders responseHeaders = headersPreparer.prepareHeaders(persistentEntity, data);

        // Check last modification for If-Modified-Since
        boolean isModified = !(eTag.matches(persistentEntity, data) || headersPreparer.isObjectStillValid(data, headers));
        // <<<

        // >>> Adapted copy from org.springframework.data.rest.webmvc.ResourceStatus.StatusAndHeaders.toResponseEntity
//        return isModified
//                ? new ResponseEntity<EntityModel<?>>(assembler.toFullResource(data), headers, HttpStatus.OK)
//                : new ResponseEntity<EntityModel<?>>(headers, HttpStatus.NOT_MODIFIED);
        // <<<

        // Customization: add link to response (LinkRelation = caption)
        if (isModified) {
            var persistentEntityResource = assembler.toFullResource(data).add(
                    Link.of("https://docs.spring.io/spring-data/rest/docs/current/reference/html/#customizing-sdr.overriding-sdr-response-handlers",
                            LinkRelation.of("Overriding Spring Data REST Response Handlers")));  // IanaLinkRelations.BOOKMARK
            return new ResponseEntity<EntityModel<?>>(persistentEntityResource, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<EntityModel<?>>(headers, HttpStatus.NOT_MODIFIED);
        }
    }

    /* org.springframework.web.servlet.DispatcherServlet.processDispatchResult
         org.springframework.web.servlet.DispatcherServlet.processHandlerException
            org.springframework.web.servlet.handler.HandlerExceptionResolverComposite.resolveException
              1. @ExceptionHandler methods are considered inside the controller
              2. By org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver - uses this.exceptionHandlerAdviceCache
                org.springframework.data.rest.webmvc.RepositoryRestExceptionHandler.handleNotFound - default handler
    */
    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<?> handleNotFound(ResourceNotFoundException exc) {
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.NOT_FOUND);
    }
}
