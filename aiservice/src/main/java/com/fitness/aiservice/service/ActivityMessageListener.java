package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repo.RecommendationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    private final ActivityAiService aiService;
    private final RecommendationRepo recommendationRepo;

    @RabbitListener(queues = "activity.queue")
    public void processActivity(Activity activity){
        try {
            log.info("Received activity for processing: {}", activity.getId());

            // Generate the recommendation
            Recommendation recommendation = aiService.generateRecommendation(activity);

            // Save it to MongoDB
            Recommendation savedRecommendation = recommendationRepo.save(recommendation);

            log.info("Successfully saved recommendation with ID: {}", savedRecommendation.getId());
            log.info("Recommendation details: {}", savedRecommendation);

        } catch (Exception e) {
            log.error("Error processing activity {}: {}", activity.getId(), e.getMessage(), e);
        }
    }
}