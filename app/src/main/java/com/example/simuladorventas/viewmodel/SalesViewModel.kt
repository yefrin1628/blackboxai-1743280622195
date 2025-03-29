@HiltViewModel
class SalesViewModel @Inject constructor(
    private val repository: SalesRepository
) : ViewModel() {

    private val _sales = MutableLiveData<List<Sale>>()
    val sales: LiveData<List<Sale>> = _sales

    private val _monthlyTotal = MutableLiveData<Double>()
    val monthlyTotal: LiveData<Double> = _monthlyTotal

    private val _state = MutableLiveData<SalesState>()
    val state: LiveData<SalesState> = _state

    init {
        loadSales()
    }

    fun loadSales() {
        _state.value = SalesState.Loading
        viewModelScope.launch {
            try {
                val sales = repository.getSales()
                _sales.value = sales
                _monthlyTotal.value = sales.sumOf { it.totalPrice() }
                _state.value = SalesState.Success
            } catch (e: Exception) {
                _state.value = SalesState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun addSale(sale: Sale) {
        viewModelScope.launch {
            try {
                repository.addSale(sale)
                loadSales()
            } catch (e: Exception) {
                _state.value = SalesState.Error("Error al agregar venta")
            }
        }
    }

    sealed class SalesState {
        object Loading : SalesState()
        object Success : SalesState()
        data class Error(val message: String) : SalesState()
    }
}