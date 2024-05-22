package live.databo3.ruleengine.ai.adaptor;

import live.databo3.ruleengine.ai.dto.OrganizationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * OpenFeign을 통한 Organization 구하는 인터페이스
 *
 * @author 박상진
 * @version 1.0.0
 */
@FeignClient(value = "account-service")
public interface OrganizationAdaptor {
    /**
     * account-service에 요청해서 OrganizationName을 받는 메서드
     * @return ResponseEntity<List<OrganizationResponse>> OrganizationName 리스트
     * @since 1.0.0
     */
    @GetMapping("/api/account/organizations")
    ResponseEntity<List<OrganizationResponse>> getOrganizations();
}
