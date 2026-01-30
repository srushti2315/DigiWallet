package com.orion.DigiWallet.repository;


import com.orion.DigiWallet.model.RewardsPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//TODO: 3.4
// Define this class as a repository for Reward entity
// Use appropriate annotations
// Hint: Use @Repository annotation
// extend JpaRepository with Reward as the entity type and Long as the ID type
@Repository
public interface RewardRepository extends JpaRepository<RewardsPoint, Long>{



}
