package co.youverify.youhr.data.mapper


//A Generic mapper definition for any type
interface Mapper<I,O>{
    fun map(input:I):O
}

//A Generic List mapper definition for any type
interface ListMapper<I,O>:Mapper<List<I>,List<O>>

//A Generic List mapper definition for any type to handle null responses from the Backend
interface NullableInputListMapper<I,O>:Mapper<List<I>?,List<O>>
interface NullableOutPutListMapper<I,O>:Mapper<List<I>,List<O>?>

class NullableInputListMapperImpl<I,O>(private val mapper: Mapper<I,O>):NullableInputListMapper<I,O> {
    override fun map(input: List<I>?): List<O> {
       return input?.map {
            mapper.map(it)
        }.orEmpty()
    }
}

class NullableOutputListMapperImpl<I,O>(private val mapper: Mapper<I,O>):NullableOutPutListMapper<I,O> {
    override fun map(input: List<I>): List<O>? {
      return  if (input.isEmpty()) null else input.map { mapper.map(it) }
    }

}