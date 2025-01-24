package bg.softuni.barberstudio.Promotion.Repository;


import bg.softuni.barberstudio.Promotion.Model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, UUID> {
}
