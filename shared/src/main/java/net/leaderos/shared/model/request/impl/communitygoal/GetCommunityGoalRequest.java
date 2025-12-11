package net.leaderos.shared.model.request.impl.communitygoal;

import net.leaderos.shared.model.request.GetRequest;

import java.io.IOException;

/**
 * GetCommunityGoalRequest class
 *
 * @author leaderos
 * @since 1.0
 */
public class GetCommunityGoalRequest extends GetRequest {

    /**
     * Request constructor
     *
     * @throws IOException for HttpUrlConnection
     */
    public GetCommunityGoalRequest() throws IOException {
        super("store/community-goal");
    }
}
