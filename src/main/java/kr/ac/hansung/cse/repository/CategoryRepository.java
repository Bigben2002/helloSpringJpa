package kr.ac.hansung.cse.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.ac.hansung.cse.model.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {

    @PersistenceContext
    private EntityManager em;

    public Category save(Category category) {
        em.persist(category);
        return category;
    }

    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(em.find(Category.class, id));
    }

    public List<Category> findAll() {
        return em.createQuery("SELECT c FROM Category c ORDER BY c.id", Category.class)
                .getResultList();
    }

    // 이름 중복 검사용 (Optional 반환 → 없으면 empty)
    public Optional<Category> findByName(String name) {
        List<Category> result = em.createQuery(
                        "SELECT c FROM Category c WHERE c.name = :name", Category.class)
                .setParameter("name", name)
                .getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    // 삭제 전 연결 상품 수 확인 (COUNT 쿼리)
    public long countProductsByCategoryId(Long categoryId) {
        return em.createQuery(
                        "SELECT COUNT(p) FROM Product p WHERE p.category.id = :id", Long.class)
                .setParameter("id", categoryId)
                .getSingleResult();
    }

    public void delete(Long id) {
        Category category = em.find(Category.class, id);
        if (category != null) {
            em.remove(category);
        }
    }

    public Optional<Category> findByIdWithProducts(Long id) {
        List<Category> result = em.createQuery(
                        "SELECT DISTINCT c FROM Category c JOIN FETCH c.products WHERE c.id = :id",
                        Category.class)
                .setParameter("id", id)
                .getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
}

//Service에서 삭제 가능 여부 판단을 위해 COUNT JPQL