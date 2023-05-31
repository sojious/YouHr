package co.youverify.youhr.data.mapper

interface Mapper<I,O>{
    fun map(input:I):O
}

interface ListMapper<I,O>:Mapper<List<I>,List<O>>
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