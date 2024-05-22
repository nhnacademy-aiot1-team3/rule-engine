package live.databo3.ruleengine.ai.adaptor;

import live.databo3.ruleengine.ai.dto.OrganizationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "account-service")
public interface OrganizationAdaptor {

    @GetMapping("/api/account/organizations")
    ResponseEntity<List<OrganizationResponse>> getOrganizations();
}
