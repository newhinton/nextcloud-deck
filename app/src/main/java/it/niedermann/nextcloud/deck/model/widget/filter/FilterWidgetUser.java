package it.niedermann.nextcloud.deck.model.widget.filter;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import it.niedermann.nextcloud.deck.model.User;

@Entity(
        indices = {
                @Index(value = "filterBoardId", name = "idx_FilterWidgetUser_filterBoardId"),
                @Index(value = "userId", name = "idx_FilterWidgetUser_userId")
        },
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "localId",
                        childColumns = "userId", onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = FilterWidgetBoard.class,
                        parentColumns = "id",
                        childColumns = "filterBoardId", onDelete = ForeignKey.CASCADE
                )
        }
)
public class FilterWidgetUser {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private Long filterBoardId;
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFilterBoardId() {
        return filterBoardId;
    }

    public void setFilterBoardId(Long filterBoardId) {
        this.filterBoardId = filterBoardId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilterWidgetUser that = (FilterWidgetUser) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (filterBoardId != null ? !filterBoardId.equals(that.filterBoardId) : that.filterBoardId != null)
            return false;
        return userId != null ? userId.equals(that.userId) : that.userId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (filterBoardId != null ? filterBoardId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
