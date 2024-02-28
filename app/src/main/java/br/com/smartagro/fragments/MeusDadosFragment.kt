package br.com.smartagro.fragments

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import br.com.smartagro.PrincipalActivity
import br.com.smartagro.R
import br.com.smartagro.clima.Cidade
import br.com.smartagro.clima.Conexao
import br.com.smartagro.clima.ConsumirXML
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import java.text.Normalizer

class MeusDadosFragment : Fragment() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    lateinit var editNome: EditText
    lateinit var editCelular: EditText
    lateinit var editCidade: EditText
    lateinit var imgBuscarCidade: ImageView

    lateinit var cidadeSelecionada : Cidade
    var cidadeList: List<Cidade> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mostrarDadosSalvos()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_meus_dados, container, false)

        editNome = view.findViewById(R.id.editNome)
        editCelular = view.findViewById(R.id.editCelular)
        editCelular.addTextChangedListener(PhoneNumberFormattingTextWatcher("BR"))
        editCidade = view.findViewById(R.id.editCidade)
        imgBuscarCidade = view.findViewById(R.id.imgBuscarCidade)
        mostrarDadosSalvos()

        var isEdit = false
        val btnEditarSalvar = view.findViewById<Button>(R.id.btnEditarSalvar)
        btnEditarSalvar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24, 0, 0, 0)


        btnEditarSalvar.setOnClickListener {
            isEdit = !isEdit
            if (isEdit) {
                imgBuscarCidade.visibility = View.VISIBLE
                btnEditarSalvar.text = "Salvar"
                btnEditarSalvar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_save_24, 0, 0, 0)
                editNome.isEnabled = true
                editNome.setBackgroundColor(resources.getColor(R.color.verde_fundo))
                editCelular.isEnabled = true
                editCelular.setBackgroundColor(resources.getColor(R.color.verde_fundo))
                editCidade.isEnabled = true
                editCidade.setBackgroundColor(resources.getColor(R.color.verde_fundo))
            } else {
                if (validarCampos()) {
                    imgBuscarCidade.visibility = View.INVISIBLE
                    editar()
                    btnEditarSalvar.text = "Editar"
                    btnEditarSalvar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24, 0, 0, 0)
                    editNome.isEnabled = false
                    editNome.setBackgroundColor(resources.getColor(R.color.white))
                    editCelular.isEnabled = false
                    editCelular.setBackgroundColor(resources.getColor(R.color.white))
                    editCidade.isEnabled = false
                    editCidade.setBackgroundColor(resources.getColor(R.color.white))
                }
            }
        }

        val imgBuscarCidade = view.findViewById<ImageView>(R.id.imgBuscarCidade)
        imgBuscarCidade.setOnClickListener {
            definirCidade()
        }

        return view
    }

    private fun definirCidade() {
        val cidade = editCidade.text.toString()
        try {
            val url = "http://servicos.cptec.inpe.br/XML/listaCidades?city=" + removerAcentosEAjustaUrl(cidade)
            Tarefa().execute(url)
        } catch (e: Exception) {
            Log.e("Erro", e.message!!)
        }
    }

    inner class Tarefa() : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg strings: String?): String {
            val dados = Conexao.getDados(strings[0])
            Log.d("Dados", "Dados: $dados")
            return dados
        }

        override fun onPostExecute(s: String?) {
            cidadeList = ConsumirXML.getCidade(s)

            if (cidadeList.isEmpty()) {
                val cidade = view?.findViewById<EditText>(R.id.editCidade)
                Toast.makeText(context, "Cidade não encontrada!", Toast.LENGTH_SHORT).show()
                if (cidade != null) {
                    cidade.setText("")
                }
            }else {
                exibirPopupSelecaoCidade(cidadeList)
            }
        }
    }

    private fun exibirPopupSelecaoCidade(cidades: List<Cidade>) {
        val nomesCidades = Array<String>(cidades.size) { "" }
        for (i in cidades.indices) {
            nomesCidades[i] = "${cidades[i].getNome()} - ${cidades[i].getUf()}"
        }

        val builder = context?.let { AlertDialog.Builder(it) }
        if (builder != null) {
            builder.setTitle("Selecione a cidade")
        }
        if (builder != null) {
            builder.setItems(nomesCidades) { dialog, which ->
                cidadeSelecionada = cidades[which]
                editCidade.setText(cidadeSelecionada.getNome() + " - " + cidadeSelecionada.getUf())
                editCidade.error = null

            }
        }
        if (builder != null) {
            builder.show()
        }
    }

    fun removerAcentosEAjustaUrl(texto: String?): String? {
        var textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD)
        textoNormalizado = textoNormalizado.replace("[^\\p{ASCII}]".toRegex(), "")
        textoNormalizado = textoNormalizado.replace(" ", "%20")
        return textoNormalizado
    }

    private fun editar() {

        editarCidadeSelecionadaNoFirestore()
        val nomeCompleto = editNome.text.toString()
        val celular = editCelular.text.toString()

        val user = firebaseAuth.currentUser
        val userId = user?.uid

        if (userId != null && user.displayName != nomeCompleto) {

            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(nomeCompleto)
                .build()

            user.updateProfile(profileUpdates)
                .addOnCompleteListener { profileTask ->
                    if (profileTask.isSuccessful) {
                        // Atualizar os dados no Firestore
                        updateUserDataInFirestore(userId, nomeCompleto,  celular)

                        Toast.makeText(requireContext(), "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show()
                        Intent(requireContext(), PrincipalActivity::class.java).apply {
                            startActivity(this)
                        }
                    } else {
                        Toast.makeText(requireContext(), "Erro ao atualizar informações do usuário.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun updateUserDataInFirestore(userId: String, nomeCompleto: String, celular: String) {
        val userData = hashMapOf(
            "nomeCompleto" to nomeCompleto,
            "celular" to celular.replace(" ", "").replace("-", "")
        )

        // Atualizar os campos no Firestore
        firestore.collection("usuarios")
            .document(userId)
            .set(userData)
            .addOnSuccessListener {
                // Campos atualizados com sucesso no Firestore
            }
            .addOnFailureListener { e ->
                // Erro ao atualizar os campos no Firestore
                Toast.makeText(requireContext(), "Erro ao atualizar os dados do usuário no Firestore.", Toast.LENGTH_SHORT).show()
            }

        // Atualizar a cidade selecionada no Firestore

    }

    private fun editarCidadeSelecionadaNoFirestore() {
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            // Exclua todos os documentos existentes na subcoleção "cidade"
            firestore.collection("usuarios")
                .document(userId)
                .collection("cidade")
                .get()
                .addOnSuccessListener { cidadeQuerySnapshot ->
                    val batch = firestore.batch()

                    for (cidadeDocument in cidadeQuerySnapshot.documents) {
                        batch.delete(cidadeDocument.reference)
                    }

                    // Adicione o novo documento representando a cidade selecionada
                    val cidadeData = hashMapOf(
                        "id" to cidadeSelecionada.getId(),
                        "nome" to cidadeSelecionada.getNome(),
                        "uf" to cidadeSelecionada.getUf()
                    )

                    batch.set(
                        firestore.collection("usuarios")
                            .document(userId)
                            .collection("cidade")
                            .document(cidadeSelecionada.getId()),
                        cidadeData
                    )

                    // Execute as operações em lote
                    batch.commit()
                        .addOnSuccessListener {
                            println("Cidades antigas removidas e nova cidade adicionada com sucesso no Firestore.")
                        }
                        .addOnFailureListener { e ->
                            println("Erro ao atualizar as cidades no Firestore: ${e.message}")
                            Log.e("FirestoreError", e.message ?: "")
                        }
                }
                .addOnFailureListener { e ->
                    println("Erro ao buscar as cidades antigas no Firestore: ${e.message}")
                    Log.e("FirestoreError", e.message ?: "")
                }
        }
    }

    private fun mostrarDadosSalvos() {
        val user = firebaseAuth.currentUser

        if (user != null) {
            val userId = user.uid
            firestore.collection("usuarios")
                .document(userId)
                .get()
                .addOnSuccessListener { userDocumentSnapshot ->
                    if (userDocumentSnapshot.exists()) {
                        val nomeCompleto = userDocumentSnapshot.getString("nomeCompleto")
                        val celular = userDocumentSnapshot.getString("celular")

                        // Preencha os campos EditText com os dados do usuário
                        editNome.setText(nomeCompleto)
                        editCelular.setText(celular)

                        // Recupere a cidade selecionada
                        firestore.collection("usuarios")
                            .document(userId)
                            .collection("cidade")
                            .get()
                            .addOnSuccessListener { cidadeQuerySnapshot ->
                                if (!cidadeQuerySnapshot.isEmpty) {
                                    // Pegue o primeiro documento da subcoleção (único documento)
                                    val cidadeDocumentSnapshot = cidadeQuerySnapshot.documents[0]

                                    val cidadeNome = cidadeDocumentSnapshot.getString("nome") + " - " + cidadeDocumentSnapshot.getString("uf")

                                    // Preencha o campo de cidade EditText
                                    editCidade.setText(cidadeNome)
                                } else {
                                    // A subcoleção não possui documentos
                                    Log.d("DadosFirebase", "A subcoleção 'cidade' não possui documentos")
                                }
                            }
                            .addOnFailureListener { e ->
                                // Erro ao buscar os dados da cidade no Firestore
                                Log.e("DadosFirebase", "Erro ao buscar dados da cidade no Firestore: ${e.message}")
                            }
                    } else {
                        // O documento do usuário não existe no Firestore
                        Log.d("DadosFirebase", "Documento do usuário não encontrado no Firestore")
                    }
                }
                .addOnFailureListener { e ->
                    // Erro ao buscar os dados do usuário no Firestore
                    Log.e("DadosFirebase", "Erro ao buscar dados do usuário no Firestore: ${e.message}")
                }
        } else {
            // O usuário não está autenticado
            Log.d("DadosFirebase", "Usuário não autenticado")
        }
    }

    private fun validarCampos(): Boolean {
        val nomeCompleto = editNome.text.toString()
        val celular = editCelular.text.toString().replace(" ", "").replace("-", "")

        val camposComProblemas = mutableListOf<EditText>()

        if (nomeCompleto.isEmpty()) {
            editNome.error = "Campo obrigatório"
            camposComProblemas.add(editNome)
        }

        if (celular.length != 10 && celular.length != 11) {
            editCelular.error = "Digite um número de celular válido no formato 99 99999-9999"
            camposComProblemas.add(editCelular)
        }

        if (celular.isEmpty()) {
            editCelular.error = "Campo obrigatório"
            camposComProblemas.add(editCelular)
        }

        if (!::cidadeSelecionada.isInitialized) {
            editCidade.error = "Clique na lupa para pesquisar uma cidade válida"
            camposComProblemas.add(editCidade)
        }

        if (camposComProblemas.isNotEmpty()) {
            camposComProblemas[0].requestFocus()
            return false
        }

        return true
    }


}