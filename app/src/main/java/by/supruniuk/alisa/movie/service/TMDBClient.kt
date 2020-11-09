package by.supruniuk.alisa.movie.service

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val API_KEY = "ad16c8e0e0197a82b75db19a22aa0578"
const val BASE_URL = "https://api.themoviedb.org/3/"

const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"

const val FIRST_PAGE = 1 //первая страница
const val POST_PER_PAGE = 20 //элементов на странице


object TMDBClient {

     fun getClient(): TMDBInterface{

            //Interceptor - Наблюдает, модифицирует и потенциально короткозамыкает запросы,
            // выходящие наружу, и соответствующие ответы, возвращающиеся обратно.
            // Обычно перехватчики добавляют, удаляют или преобразуют заголовки запроса или ответа.
            //Создание перехватчика для добавления запроса api_key перед всеми запросами/во всех запросах в качестве перехватчика запросов
            val requestInterceptor = Interceptor{ chain ->
                //Перехватчик принимает только один аргумент, который является лямбда-функцией, поэтому скобки можно опустить/опущены

                val newUrl = chain.request()
                    .url
                    .newBuilder()
                    .addQueryParameter("api_key", API_KEY)
                    .build()

                val newRequest = chain.request()
                    .newBuilder()
                    .url(newUrl) //с нашим url
                    .build()
                //proceed - продолжить
                //return@Interceptor
                chain.proceed(newRequest) //явно возвращать значение из Interceptor с @ аннотацией. Лямбда всегда возвращает значение лямбды
            }
            //создание сетевого клиента с помощью OkHttp и добавление нашего requestInterceptor
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                //Используются таймауты, чтобы прервать вызов, когда его партнер недоступен
                //.connectTimeout(60, TimeUnit.SECONDS) //настраивается таймаут в 60 секунд, сколько будет ждать ответ
                .build()

            //объединяем все вместе, чтобы создать конструктор и обработчик HTTP-запросов
            // с помощью Retrofit.
            // Здесь добавляем ранее созданный сетевой клиент,
            // базовый URL
            // добавляем конвертер
            // и фабрику адаптеров.
            // объект Retrofit, содержащий базовый URL и
            // способность преобразовывать JSON-данные с помощью указанного конвертера Gson.
            //
            //Далее в его методе create() указываем наш класс интерфейса с запросами к сайту.
            return  Retrofit.Builder() //собирается объект, в кот. устанавливается
                .client(okHttpClient) //клиент - это то как библиотека будет работать с сетью(в нем настроен таймаут и перехватчик запросов
                .baseUrl(BASE_URL) //адрес относительно которого будут идти запросы
                //       .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//интеграции с библиотекой RxJava, т.е. потоки для получения и отправки данных ..заменить на
                .addConverterFactory(GsonConverterFactory.create())//с помощью этого метода добавляется (фабрика)конвертер(собирать/ разбирать объекты, кот.приходят с сервера/
                .addCallAdapterFactory(CoroutineCallAdapterFactory()) //корутина
                .build() //создается объект со всеми настройками
                .create(TMDBInterface::class.java) //создание запросов/обработка //создается экземпляр класса которому делали все аннотации для работы с сетью
        }
}