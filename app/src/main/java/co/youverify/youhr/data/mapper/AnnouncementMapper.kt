package co.youverify.youhr.data.mapper

import co.youverify.youhr.data.model.AnnouncementDto
import co.youverify.youhr.domain.model.Announcement

class DtoToDomainAnnouncementMapper:Mapper<AnnouncementDto,Announcement>{
    override fun map(input: AnnouncementDto): Announcement {
        return Announcement(
            postedBy = input.postedBy,
            message = input.announcement,
            createdAt = input.createdAt,
            title = input.subject,
            postedByDisplayPicUrl = input.userId.displayPicture
        )
    }

}

class DtoToDomainAnnouncementListMapper(val mapper: DtoToDomainAnnouncementMapper):NullableInputListMapper<AnnouncementDto,Announcement>{
    override fun map(input: List<AnnouncementDto>?): List<Announcement> {
        return input?.map {
            mapper.map(it)
        }?: emptyList()
    }


}