package com.example.leolira.bingoleo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Integer> numerosDisponiveis;
    private ArrayList<Integer> numerosSorteados;
    private ArrayAdapter<Integer> adapterSorteados;
    private ArrayAdapter<Integer> adapterDisponiveis;
    private Integer ultimoNumero = 0;
    private Random geradorRandomico;
    private ImageView imageViewBola;
    private TextView textViewBola;
    private TextView textViewListaSorteados;
    private TextView textViewListaDisponiveis;
    private ListView listViewSorteados;
    private ListView listViewDisponiveis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        geradorRandomico = new Random(System.currentTimeMillis());
        imageViewBola = findViewById(R.id.imageViewBola);
        textViewBola = findViewById(R.id.textViewBola);
        listViewSorteados = findViewById(R.id.listViewSorteados);
        listViewDisponiveis = findViewById(R.id.listViewDisponiveis);
        textViewListaSorteados = findViewById(R.id.textViewListaSorteados);
        textViewListaDisponiveis = findViewById(R.id.textViewListaDisponiveis);

        if (numerosDisponiveis == null) {
            numerosDisponiveis = new ArrayList<>();
            for (int i = 0; i < 75; i++) {
                numerosDisponiveis.add(i + 1);
            }
        }

        if (numerosSorteados == null) {
            numerosSorteados = new ArrayList<>();
        }

        if (adapterSorteados == null) {
            adapterSorteados = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numerosSorteados);
            listViewSorteados.setAdapter(adapterSorteados);
        }
        if (adapterDisponiveis == null) {
            adapterDisponiveis = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numerosDisponiveis);
            listViewDisponiveis.setAdapter(adapterDisponiveis);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putIntegerArrayList("SORTEADOS", numerosSorteados);
        outState.putIntegerArrayList("DISPONIVEIS", numerosDisponiveis);
        outState.putString("ULTIMONUMERO", ultimoNumero.toString());
    }

    @SuppressWarnings("UnusedReturnValue")
    private boolean limpaContexto() {

        try {
            numerosDisponiveis = new ArrayList<>();
            for (int i = 0; i < 75; i++) {
                numerosDisponiveis.add(i + 1);
            }
            numerosSorteados = new ArrayList<>();
            adapterSorteados = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numerosSorteados);
            listViewSorteados.setAdapter(adapterSorteados);
            adapterDisponiveis = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numerosDisponiveis);
            listViewDisponiveis.setAdapter(adapterDisponiveis);
            ultimoNumero = 0;
            textViewBola.setText("");
            imageViewBola.setVisibility(View.GONE);
            textViewBola.setVisibility(View.GONE);
            textViewListaDisponiveis.setVisibility(View.GONE);
            textViewListaSorteados.setVisibility(View.GONE);
            listViewDisponiveis.setVisibility(View.GONE);
            listViewSorteados.setVisibility(View.GONE);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("UnusedReturnValue")
    private boolean restauraContexto(Bundle savedInstanceState) {
        try {
            if (savedInstanceState != null) {
                numerosSorteados = savedInstanceState.getIntegerArrayList("SORTEADOS");
                numerosDisponiveis = savedInstanceState.getIntegerArrayList("DISPONIVEIS");
                ultimoNumero = Integer.parseInt(savedInstanceState.get("ULTIMONUMERO").toString());

                exibeOcultaControles();

                adapterSorteados = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numerosSorteados);
                listViewSorteados.setAdapter(adapterSorteados);
                adapterSorteados.notifyDataSetChanged();

                adapterDisponiveis = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numerosDisponiveis);
                listViewDisponiveis.setAdapter(adapterDisponiveis);
                adapterDisponiveis.notifyDataSetChanged();
            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restauraContexto(savedInstanceState);
    }

    public void buttonLimpar(View botao)
    {
        if (botao.getId() == R.id.buttonLimpar) {
            limpaContexto();
        }
    }

    private void exibeOcultaControles()
    {
        if (ultimoNumero > 0) {

            imageViewBola.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("ball_" + (ultimoNumero - 1) / 15, "drawable", getPackageName())));

            textViewBola.setText(String.format(Locale.getDefault(),"%02d",ultimoNumero));
            imageViewBola.setVisibility(View.VISIBLE);
            textViewBola.setVisibility(View.VISIBLE);
            textViewListaDisponiveis.setVisibility(View.VISIBLE);
            textViewListaSorteados.setVisibility(View.VISIBLE);
            listViewDisponiveis.setVisibility(View.VISIBLE);
            listViewSorteados.setVisibility(View.VISIBLE);
        } else {
            imageViewBola.setVisibility(View.GONE);
            textViewBola.setVisibility(View.GONE);
            textViewListaDisponiveis.setVisibility(View.GONE);
            textViewListaSorteados.setVisibility(View.GONE);
            listViewDisponiveis.setVisibility(View.GONE);
            listViewSorteados.setVisibility(View.GONE);
        }
    }
    public void buttonSortear(View botao) {



        if (botao.getId() == R.id.buttonSortear) {
            if (numerosDisponiveis.size() > 0) {
                Integer posicao_sorteio = geradorRandomico.nextInt(numerosDisponiveis.size());
                ultimoNumero = numerosDisponiveis.get(posicao_sorteio);

                numerosSorteados.add(ultimoNumero);
                numerosDisponiveis.remove(ultimoNumero);
                textViewBola.setText(String.format(Locale.getDefault(),"%02d",ultimoNumero));

                adapterSorteados.notifyDataSetChanged();
                adapterDisponiveis.notifyDataSetChanged();
                exibeOcultaControles();

            } else {
                Toast.makeText(this, "Todos os números já foram sorteados!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
