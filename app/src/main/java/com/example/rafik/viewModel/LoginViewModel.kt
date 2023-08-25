package com.example.rafik.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.rafik.data.repo.FireBaseRepoImpl
import com.example.rafik.domian.entity.Area
import com.example.rafik.domian.entity.City
import com.example.rafik.domian.entity.FirebaseUserLiveData
import com.example.rafik.domian.entity.User
import kotlinx.coroutines.launch

@SuppressLint("CheckResult")
class LoginViewModel : ViewModel() {
    private val firebaseRepoImpl = FireBaseRepoImpl()
    var cities: List<City> = listOf(
        City(
            "الإسكندرية", "Alexandria",
            listOf(
                Area("ابو قير"),
                Area("اسكندريه"),
                Area("الابراهيمية"),
                Area("الازاريطه"),
                Area("الانفوشى"),
                Area("الحضره"),
                Area("الدخيله"),
                Area("السرايا"),
                Area("السيوف"),
                Area("الشاطبى"),
                Area("العجمى"),
                Area("العصافره"),
                Area("العطارين"),
                Area("القبارى"),
                Area("اللبان"),
                Area("المعموره"),
                Area("المكس"),
                Area("المنتزه"),
                Area("المندره"),
                Area("المنشية"),
                Area("باكوس"),
                Area("بحرى"),
                Area("بولكلى"),
                Area("ثروت"),
                Area("جليم"),
                Area("جناكليس"),
                Area("حى الجمرك"),
                Area("حى العامرية"),
                Area("حى كليوباترا"),
                Area("حى ميامى"),
                Area("رأس التين"),
                Area("رشدى"),
                Area("زيزينيا"),
                Area("سابا باشا"),
                Area("سان ستيفانو"),
                Area("سبورتنج"),
                Area("ستانلى"),
                Area("سموحه"),
                Area("سيدى بشر"),
                Area("سيدى جابر"),
                Area("شدس"),
                Area("صفر"),
                Area("فكتوريا"),
                Area("فلمنج"),
                Area("كامب شيزار"),
                Area("كرموز"),
                Area("كفر عبده"),
                Area("كوم الدكة"),
                Area("لوران"),
                Area("محرم بك"),
                Area("محطة الرمل")
            )
        ),
        City(
            "الإسماعيلية", "Ismailia",
            listOf(
                Area(" مركز أبو صوير"),
                Area("مركز الإسماعيلية"),
                Area("مركز التل الكبير"),
                Area("مركز فايد"),
                Area("مركز القصاصين"),
                Area("مركز القنطرة شرق"),
                Area("مركز القنطرة غرب"),
            )
        ),
        City(
            "أسوان", "Aswan",
            listOf(
                Area("مركز أسوان"),
                Area("مركز إدفو"),
                Area("مركز دراو"),
                Area("مركز كوم أمبو"),
                Area("مركز نصر النوبة"),
            )
        ),
        City(
            "أسيوط", "Asyut",
            listOf(
                Area("مركز أبنوب"),
                Area("مركز أبو تيج"),
                Area("مركز أسيوط"),
                Area("مركز البداري"),
                Area("مركز ديروط"),
                Area("مركز ساحل سليم"),
                Area("مركز صدفا"),
                Area("مركز الغنايم"),
                Area("مركز الفتح"),
                Area("ركز القوصية"),
                Area("مركز منفلوط")
            )
        ),
        City(
            "الأقصر", "Luxor",
            listOf(
                Area("مدينة الأقصر"),
                Area("مدينة الأقصر الجديدة"),
                Area("مدينة طيبة الجديدة"),
                Area("مركز الزينية"),
                Area("مركز البياضية"),
                Area("مركز القرنة"),
                Area("مركز أرمنت"),
                Area("مركز الطود"),
                Area("مركز إسنا"),
            )
        ),
        City(
            "البحر الأحمر", "The Red Sea",
            listOf(
                Area("حلايب"),
                Area("رأس شقير"),
                Area("رأس غارب"),
                Area("سفاجا"),
                Area("الشلاتين"),
                Area("الغردقة"),
                Area("القصير"),
                Area("مرسى علم")
            )
        ),
        City(
            "البحيرة", "Beheira ",
            listOf(
                Area("رشيد"),
                Area("شبراخيت"),
                Area("إيتاي البارود"),
                Area("أبو حمص"),
                Area("حوش عيسى"),
                Area("كفر الدوار"),
                Area("الدلنجات"),
                Area("كوم حمادة"),
                Area("دمنهور"),
                Area("المحمودية"),
                Area("إدكو"),
                Area("أبو المطامير"),
                Area("الرحمانية"),
                Area("النوبارية الجديدة"),
                Area("وادي النطرون"),
                Area("بدر")
            )
        ),
        City(
            "بني سويف", "Bani Sweif",
            listOf(
                Area("إهناسيا"),
                Area("ببا"),
                Area("بني سويف"),
                Area("الفشن"),
                Area("ناصر"),
                Area("الواسطى")
            )
        ),
        City(
            "بورسعيد", "Port Said",
            listOf(
                Area("حى الشرق"),
                Area("حى المناخ"),
                Area("العرب"),
                Area("حى الضواحى"),
                Area("بور فؤاد"),
                Area("حى الزهور"),
                Area("المنطقة الحرة"),
                Area("بور توفيق"),
                Area("حى مبارك"),
                Area("جنوب بورسعيد"),
                Area("حى الافرنج"),
                Area("حى الكويت"),
                Area("المنشية"),
                Area("الثلاثينى"),
                Area("المنطقة الصناعية"),
                Area(" العاشر من رمضان"),
                Area("حى الجولف"),
                Area("الجمهورية"),
                Area("المناصرة"),
                Area("شرق التفريعة"),
                Area("الرسوة")
            )
        ),
        City(
            "جنوب سيناء", "South of Sinaa",
            listOf(
                Area("أبو رديس"),
                Area("أبو زنيمة"),
                Area("نويبع"),
                Area("طابا"),
                Area("رأس سدر"),
                Area("دهب"),
                Area("شرم الشيخ"),
                Area("سانت كاترين"),
                Area("الطور"),
            )
        ),
        City(
            "الجيزة", "Giza",
            listOf(
                Area("منشأة القناطر"),
                Area("أوسيم"),
                Area("كرداسة"),
                Area("أبو النمرس"),
                Area("الحوامدية"),
                Area("البدرشين"),
                Area("العياط"),
                Area("الصف"),
                Area("أطفيح"),
                Area("الواحات البحرية"),
                Area("جيزه")
            )
        ),
        City(
            "الدقهلية", "Dakahlia",
            listOf(
                Area("أجا"),
                Area("الستاموني"),
                Area("بلقاس"),
                Area("بني عبيد"),
                Area("تمي الأمديد"),
                Area(" الجمالية"),
                Area("دكرنس"),
                Area("السنبلاوين"),
                Area("شربين"),
                Area("طلخا"),
                Area("لكردي"),
                Area("محلة دمنة"),
                Area("المطرية"),
                Area("المنزلة"),
                Area("المنصورة"),
                Area("منية النصر"),
                Area("ميت سلسيل"),
                Area("ميت غمر"),
                Area("نبروه")
            )
        ),
        City(
            "دمياط", "Damietta",
            listOf(
                Area("دمياط"),
                Area("الزرقا"),
                Area("فارسكور"),
                Area("كفر سعد"),
                Area("كفر البطيخ"),
            )
        ),
        City(
            "سوهاج", "Sohag",
            listOf(
                Area("سوهاج"),
                Area("خميم"),
                Area("البلينا"),
                Area("لمراغة"),
                Area("المنشأة"),
                Area("دار السلام"),
                Area("جرجا"),
                Area("جهينة"),
                Area("ساقلته"),
                Area("طما"),
                Area("طهطا"),
                Area("العسيرات"),
            )
        ),
        City(
            "السويس", "Suez",
            listOf(
                Area("السويس"),
                Area("الأربعين"),
                Area("عتاقة"),
                Area("الجناين"),
                Area("فيصل")
            )
        ),
        City(
            "الشرقية", "Al Sharqiyah",
            listOf(
                Area("الزقازيق "),
                Area("الحسينية"),
                Area("فاقوس"),
                Area("بلبيس"),
                Area("منيا القمح"),
                Area("أبو حماد"),
                Area("ولاد صقر"),
                Area("لعاشر من رمضان"),
                Area("الصالحية الجديدة"),
                Area("كفر صقر"),
                Area("أبو كبير"),
                Area("القنايات"),
                Area("مشتول السوق"),
                Area("ديرب نجم"),
                Area("الإبراهيمية"),
                Area("ههيا"),
                Area("القرين"),
                Area("صان الحجر"),
                Area("العزيزية"),
            )
        ),
        City(
            "شمال سيناء", "North Sinai",
            listOf(
                Area("العريش"),
                Area("بئر العبد"),
                Area("الحسنة"),
                Area("نخل"),
                Area("الشيخ زويد"),
                Area("رفح"),
            )
        ),
        City(
            "الغربية", "Al Gharbiyyah",
            listOf(
                Area(" بسيون"),
                Area("زفتى"),
                Area("سمنو"),
                Area("السنطة"),
                Area("طنطا"),
                Area("قطور"),
                Area("كفر الزيات"),
                Area("المحلة الكبري"),
            )
        ),
        City(
            "الفيوم", "Fayoum",
            listOf(
                Area("إبشواي"),
                Area("إطسا"),
                Area("سنورس"),
                Area("طامية"),
                Area("الفيوم"),
                Area("يوسف الصديق"),
            )
        ),
        City(
            "القاهرة", "Cairo",
            listOf(
                Area("السلام الاول"),
                Area("السلام التاني"),
                Area("المرج"),
                Area("المطرية"),
                Area("النزهة"),
                Area("عين شمس"),
                Area("مدينة نصر"),
                Area("مصر الجديده"),
                Area("الازبكية"),
                Area("الموسكي"),
                Area("الوايلي"),
                Area("باب الشعرية"),
                Area("بولاق"),
                Area("عابدين"),
                Area("منشأة ناصر"),
                Area("15 مايو"),
                Area("البساتين"),
                Area("التبين"),
                Area("الخليفة"),
                Area("السيده زينب"),
                Area("المعادي"),
                Area("المعصرة"),
                Area(" المقطم"),
                Area(" حلوان"),
                Area("دار السلام"),
                Area("طرة"),
                Area("مصر القديمة"),
                Area("الاميرية"),
                Area("الزاوية الحمرا"),
                Area("الزيتون"),
                Area("الشرابية"),
                Area("حدائق القبة"),
                Area("روض الفرج"),
                Area("شبرا")
            )
        ),
        City(
            "القليوبية", "Qalyubia",
            listOf(
                Area("بنها"),
                Area("قليوب"),
                Area("القناطر الخيرية"),
                Area("شبرا الخيمة"),
                Area("الخانكة"),
                Area("كفر شكر"),
                Area("شبين القناطر"),
                Area("طوخ"),
                Area("العبور"),
                Area("قها"),
                Area("الخصوص"),
            )
        ),
        City(
            "قنا", "Qena",
            listOf(
                Area("أبو تشت "),
                Area("دشنا"),
                Area("فرشوط"),
                Area("قفط"),
                Area("قنا"),
                Area("قوص"),
                Area("نجع حمادي"),
                Area("نقادة"),
                Area("الوقف"),
            )
        ),
        City(
            "كفر الشيخ", "Kafr El-Sheikh",
            listOf(
                Area("البرلس"),
                Area("بيلا"),
                Area("الحامول"),
                Area("دسوق"),
                Area("الرياض"),
                Area("سيدي سلم"),
                Area("فوه"),
                Area("قلين"),
                Area("كفر الشيخ"),
                Area("مطوبس"),
            )
        ),
        City(
            "مطروح", "Matrouh",
            listOf(
                Area("الحمام"),
                Area("العلمين"),
                Area("الضبعة"),
                Area("مرسي مطروح"),
                Area("النجيلة"),
                Area("براني"),
                Area("السلوم"),
                Area("سيوة"),
            )
        ),
        City(
            "المنوفية", "Menoufia",
            listOf(
                Area("أشمون"),
                Area("الباجور"),
                Area("بركة السبع"),
                Area("منوف"),
                Area("مدينة السادات"),
                Area("سرس الليان"),
                Area("تلا"),
                Area("الشهداء"),
                Area("شبين الكوم"),
                Area("قويسنا"),
            )
        ),
        City(
            "المنيا", "Minya",
            listOf(
                Area("مدينة المنيا"),
                Area("العدوة"),
                Area("مغاغة"),
                Area("بني مزار"),
                Area("مطاي"),
                Area("سمالوط"),
                Area("قرقاص"),
                Area("ملوي"),
                Area("ديرمواس"),
                Area("ابو قرقاص"),
            )
        ),
        City(
            "الوادي الجديد", "The New Valley",
            listOf(
                Area("باريس"),
                Area("بلاط"),
                Area("الخارجة"),
                Area("الداخلة"),
                Area("الفرافرة"),
            )
        ),
    )
    private val _username = MutableLiveData<String?>()
    val username: LiveData<String?>
        get() = _username

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?>
        get() = _user

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean>
        get() = _isLogin

    private val _authenticationState = MutableLiveData<AuthenticationState?>()
    val authenticationState: LiveData<AuthenticationState?>
        get() = _authenticationState

    fun postUser(user: User) {
        _user.value = user
        viewModelScope.launch {
            _authenticationState.value = AuthenticationState.AUTHENTICATED
        }
    }

    fun loginUser(user: User) {
        _user.value = user
        _isLogin.value = true
        _isLogin.postValue(true)
        viewModelScope.launch {
            _authenticationState.value = AuthenticationState.AUTHENTICATED
        }
    }


    fun setUser(user: User) {
        viewModelScope.launch {
            firebaseRepoImpl.setUser(user)
            _authenticationState.value = AuthenticationState.AUTHENTICATED
        }
    }

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    init {
        _isLogin.value=false
        FirebaseUserLiveData().map { user ->
            if (user != null) {
                _authenticationState.value = AuthenticationState.AUTHENTICATED
                _username.value = user.displayName
            } else {
                _authenticationState.value = AuthenticationState.UNAUTHENTICATED
            }
        }
    }

}