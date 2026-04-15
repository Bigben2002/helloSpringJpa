package kr.ac.hansung.cse.service;

import kr.ac.hansung.cse.exception.DuplicateCategoryException;
import kr.ac.hansung.cse.model.Category;
import kr.ac.hansung.cse.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;    // final에 대한 생성자 부재 이슈

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    // 카테고리 전체 조회 사용
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    //카테고리 생성
    @Transactional  // readOnly 오버라이드 → 쓰기 허용
    public Category createCategory(String name) {
        // 중복 검사: 이름이 이미 있으면 예외 발생
        String normalized = name == null ? null : name.trim();
        // 중복 검사
        categoryRepository.findByName(normalized)
                .ifPresent(c -> {
                    throw new DuplicateCategoryException(normalized);
                });

        return categoryRepository.save(new Category(normalized));
    }
    // 카테고리 삭제
    @Transactional
    public void deleteCategory(Long id) {
        long count = categoryRepository.countProductsByCategoryId(id);
        if (count > 0) {
            throw new IllegalStateException("상품 " + count + "개가 연결되어 있어 삭제할 수 없습니다.");
        }
        categoryRepository.delete(id);
    }
}

// 클래스 기본 readOnly=true, 쓰기 메서드만 @Transactional로 override
// 클래스 기본: 읽기 전용 — Dirty Checking 비활성화 → 성능 향상 (5-2 JPA 프레임워크 참고)
// ── 쓰기 — @Transactional 오버라이드 (readOnly = false) ─────