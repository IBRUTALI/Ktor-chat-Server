package com.ighorosipov.data

//import com.ighorosipov.data.model.Message
//import org.bson.conversions.Bson
//import org.litote.kmongo.coroutine.CoroutineDatabase
//
//class MessageDataSourceImpl(
//    private val db: CoroutineDatabase
//): MessageDataSource {
//
//    private val messages = db.getCollection<Message>()
//
//    override suspend fun getAllMessages(): List<Message> {
//        return messages.find()
//            .descendingSort(Message::timestamp)
//            .toList()
//    }
//
//    override suspend fun insertMessage(message: Message) {
//        messages.insertOne(message)
//    }
//
//    override suspend fun getLastMessage(): Message? {
//        return messages.find()
//            .descendingSort(Message::id)
//            .limit(1)
//            .first()
//    }
//
//    override suspend fun deleteMessages(selectedMessages: List<Message>) {
//       // messages.deleteMany(Bson)
//    }
//
//    override suspend fun updateMessage(message: Message) {
//
//    }
//
//
//}