package it.niedermann.nextcloud.deck.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

@Entity(
        primaryKeys = {"userId", "boardId"},
        indices = {@Index("boardId"), @Index("userId")},
        foreignKeys = {
                @ForeignKey(entity = Board.class,
                        parentColumns = "localId",
                        childColumns = "boardId"),
                @ForeignKey(entity = User.class,
                        parentColumns = "localId",
                        childColumns = "userId")
        })
public class JoinBoardWithUser {
    @NonNull
    private Long userId;
    @NonNull
    private Long boardId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JoinBoardWithUser that = (JoinBoardWithUser) o;

        if (!userId.equals(that.userId)) return false;
        return boardId.equals(that.boardId);
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + boardId.hashCode();
        return result;
    }
}
