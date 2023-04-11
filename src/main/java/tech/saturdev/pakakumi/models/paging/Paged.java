package tech.saturdev.pakakumi.models.paging;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paged<T> {
    private Page<T> page;
    private Paging paging;
}
