package kr.ac.hansung.cse.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryForm {

    @NotBlank(message = "카테고리 이름을 입력하세요.")
    @Size(max = 50, message = "카테고리 이름은 50자 이내로 입력하세요.")
    private String name;
}
//엔티티(Category)와 폼 검증 규칙을 분리