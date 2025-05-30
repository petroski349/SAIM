PGDMP  8    7                }         	   trab_saim    17.0    17.0 O    s           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            t           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            u           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            v           1262    17237 	   trab_saim    DATABASE     �   CREATE DATABASE trab_saim WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Portuguese_Brazil.1252';
    DROP DATABASE trab_saim;
                     postgres    false            �            1259    17277    atualiza_itens    TABLE     �   CREATE TABLE public.atualiza_itens (
    "Atualiza_ID" integer[],
    "Estoque_ID" integer[],
    "Prod_Cod" integer[],
    "Qtde_Anterior" integer[],
    "Qtde_Nova" integer[],
    "Data_Atualizacao" date[]
);
 "   DROP TABLE public.atualiza_itens;
       public         heap r       postgres    false            �            1259    17347    atualiza_produtos    TABLE     �   CREATE TABLE public.atualiza_produtos (
    "AtuRec_ID" integer[],
    "Doacao_ID" integer[],
    "ProdCod" integer[],
    "DataAtualizacao" date[],
    "ProdQtde" numeric[],
    "FuncCod" integer[]
);
 %   DROP TABLE public.atualiza_produtos;
       public         heap r       postgres    false            �            1259    17342    atualiza_receita    TABLE     �   CREATE TABLE public.atualiza_receita (
    "AtuRec_ID" integer[],
    "Receita_ID" integer[],
    "ProdCod" integer[],
    "DataAtualizacao" date[],
    "TipoAtualizacao" date[],
    "FuncCod" integer[]
);
 $   DROP TABLE public.atualiza_receita;
       public         heap r       postgres    false            �            1259    17268    caixa    TABLE     s   CREATE TABLE public.caixa (
    "Caixa_ID" integer[],
    "Saldo" integer[],
    "Ultima_Atualizacao" integer[]
);
    DROP TABLE public.caixa;
       public         heap r       postgres    false            �            1259    17262    compra    TABLE     �   CREATE TABLE public.compra (
    "Compra_ID" integer[],
    "FuncCod" integer[],
    "Compra_Data" date[],
    "Compra_Valor_Total" integer[]
);
    DROP TABLE public.compra;
       public         heap r       postgres    false            �            1259    17395    consulta    TABLE     �   CREATE TABLE public.consulta (
    concod integer NOT NULL,
    medid integer NOT NULL,
    paciconvenio character varying(100),
    contipo character varying(20),
    condata date
);
    DROP TABLE public.consulta;
       public         heap r       postgres    false            �            1259    17394    consulta_concod_seq    SEQUENCE     �   CREATE SEQUENCE public.consulta_concod_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.consulta_concod_seq;
       public               postgres    false    236            w           0    0    consulta_concod_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.consulta_concod_seq OWNED BY public.consulta.concod;
          public               postgres    false    235            �            1259    17324    contas    TABLE     �   CREATE TABLE public.contas (
    "Conta_ID" integer[],
    "Conta_Tipo" "char"[],
    "Conta_Data" date[],
    "Conta_Valor" integer[],
    "Conta_Status" "char"[]
);
    DROP TABLE public.contas;
       public         heap r       postgres    false            �            1259    17329    despesas    TABLE     �   CREATE TABLE public.despesas (
    "Despesa_ID" integer[],
    "Despesa_Tipo" "char"[],
    "Despesa_Valor" integer[],
    "Despesa_Data" date[]
);
    DROP TABLE public.despesas;
       public         heap r       postgres    false            �            1259    17283    doacao    TABLE     �   CREATE TABLE public.doacao (
    "Doacao_ID" integer[],
    "Uso_ID" integer[],
    "FuncCod" integer[],
    "Data_Doacao" date[]
);
    DROP TABLE public.doacao;
       public         heap r       postgres    false            �            1259    17241    funcionario    TABLE       CREATE TABLE public.funcionario (
    "FuncCod" integer[],
    "FuncNome" "char"[],
    "FuncCPF" integer[],
    "FuncCargo" "char"[],
    "FuncRG" integer[],
    "FuncEndereco" "char"[],
    "FuncTelefone" "char"[],
    "FuncData_Admissao" date[],
    "FuncData_Demissao" date[]
);
    DROP TABLE public.funcionario;
       public         heap r       postgres    false            �            1259    17406 
   internacao    TABLE     �   CREATE TABLE public.internacao (
    prontuario_id integer NOT NULL,
    pacinome character varying(100) NOT NULL,
    dataentrada date NOT NULL,
    datasaida date
);
    DROP TABLE public.internacao;
       public         heap r       postgres    false            �            1259    17405    internacao_prontuario_id_seq    SEQUENCE     �   CREATE SEQUENCE public.internacao_prontuario_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 3   DROP SEQUENCE public.internacao_prontuario_id_seq;
       public               postgres    false    238            x           0    0    internacao_prontuario_id_seq    SEQUENCE OWNED BY     ]   ALTER SEQUENCE public.internacao_prontuario_id_seq OWNED BY public.internacao.prontuario_id;
          public               postgres    false    237            �            1259    17429    med_consulta    TABLE     �  CREATE TABLE public.med_consulta (
    id integer NOT NULL,
    medcrm integer,
    mednome character varying(100),
    medespecialidade character varying(100),
    condata date,
    horarioconsulta integer,
    pacinome character varying(100),
    pacicpf character varying(14),
    paciconvenio integer,
    numconvenio integer,
    dataultimaconsulta date,
    contipo character(1)
);
     DROP TABLE public.med_consulta;
       public         heap r       postgres    false            �            1259    17428    med_consulta_id_seq    SEQUENCE     �   CREATE SEQUENCE public.med_consulta_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.med_consulta_id_seq;
       public               postgres    false    240            y           0    0    med_consulta_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.med_consulta_id_seq OWNED BY public.med_consulta.id;
          public               postgres    false    239            �            1259    17250    medico    TABLE     �   CREATE TABLE public.medico (
    "MedID" integer[],
    "MedNome" "char"[],
    "MedCRM" integer[],
    "Med_Horario_Atendimento" "char"[],
    "FuncCod" integer[],
    "MedTelefone" "char"[]
);
    DROP TABLE public.medico;
       public         heap r       postgres    false            �            1259    17352    movi_compra_prod    TABLE     �   CREATE TABLE public.movi_compra_prod (
    "Compra_ID" integer[],
    "ProdCod" integer[],
    "ProdQtde" numeric[],
    "Compra_Valor_Total" numeric[]
);
 $   DROP TABLE public.movi_compra_prod;
       public         heap r       postgres    false            �            1259    17265    movimentacao_caixa    TABLE     �   CREATE TABLE public.movimentacao_caixa (
    "MoviC_ID" integer[],
    "MoviC_Tipo" "char"[],
    "MoviC_Valor" integer[],
    "MoviC_Data" date[],
    "Compra_ID" integer[],
    "Caixa_ID" integer[]
);
 &   DROP TABLE public.movimentacao_caixa;
       public         heap r       postgres    false            �            1259    17480    paciente    TABLE     �  CREATE TABLE public.paciente (
    paciid integer NOT NULL,
    pacinome character varying(100) NOT NULL,
    pacicpf character varying(14) NOT NULL,
    paciconvenio character varying(100),
    pacidata_internacao date,
    pacidata_alta date,
    paciendereco character varying(200),
    pacirg character varying(20),
    pacitelefone character varying(20),
    pacidata_nascimento date,
    pacicep character varying(20)
);
    DROP TABLE public.paciente;
       public         heap r       postgres    false            �            1259    17479    paciente_paciid_seq    SEQUENCE     �   CREATE SEQUENCE public.paciente_paciid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.paciente_paciid_seq;
       public               postgres    false    244            z           0    0    paciente_paciid_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.paciente_paciid_seq OWNED BY public.paciente.paciid;
          public               postgres    false    243            �            1259    17274    produtos    TABLE     �   CREATE TABLE public.produtos (
    "ProdCod" integer[],
    "ProdNome" "char"[],
    "ProdQtde" integer[],
    "ProdCategoria" "char"[],
    "ProdDescricao" "char"[],
    "ProdEstoque_Minimo" integer[]
);
    DROP TABLE public.produtos;
       public         heap r       postgres    false            �            1259    17253 
   prontuario    TABLE     �   CREATE TABLE public.prontuario (
    "Prontuario_ID" integer[],
    "MedCRM" integer[],
    "PaciNome" "char"[],
    "PaciID" integer[],
    "Data_Abertura" date[]
);
    DROP TABLE public.prontuario;
       public         heap r       postgres    false            �            1259    17334    receita    TABLE     �   CREATE TABLE public.receita (
    "Receita_ID" integer[],
    "MedID" integer[],
    "PaciID" integer[],
    "FuncCod" integer[],
    "DataReceita" date[]
);
    DROP TABLE public.receita;
       public         heap r       postgres    false            �            1259    17238    usuario    TABLE     �   CREATE TABLE public.usuario (
    "Usu_Tipo" "char"[],
    "Usu_login" "char"[],
    "Usu_Senha" "char"[],
    "Usu_Data_Registro" date[]
);
    DROP TABLE public.usuario;
       public         heap r       postgres    false            �            1259    17382    visita    TABLE     �   CREATE TABLE public.visita (
    visicod integer NOT NULL,
    visinome character varying(100) NOT NULL,
    visicpf character varying(20) NOT NULL,
    visidata_entrada date NOT NULL,
    visidata_saida date
);
    DROP TABLE public.visita;
       public         heap r       postgres    false            �            1259    17385    visita_visicod_seq    SEQUENCE     �   CREATE SEQUENCE public.visita_visicod_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.visita_visicod_seq;
       public               postgres    false    233            {           0    0    visita_visicod_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.visita_visicod_seq OWNED BY public.visita.visicod;
          public               postgres    false    234            �            1259    17437 	   visitante    TABLE     m  CREATE TABLE public.visitante (
    visicod integer NOT NULL,
    visinome character varying(100) NOT NULL,
    visicpf character varying(14) NOT NULL,
    visiemail character varying(100),
    visitelefone character varying(20),
    visiendereco character varying(200),
    visicep character varying(9),
    visidata_cadastro date,
    visidata_nascimento date
);
    DROP TABLE public.visitante;
       public         heap r       postgres    false            �            1259    17436    visitante_visicod_seq    SEQUENCE     �   CREATE SEQUENCE public.visitante_visicod_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.visitante_visicod_seq;
       public               postgres    false    242            |           0    0    visitante_visicod_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.visitante_visicod_seq OWNED BY public.visitante.visicod;
          public               postgres    false    241            �           2604    17398    consulta concod    DEFAULT     r   ALTER TABLE ONLY public.consulta ALTER COLUMN concod SET DEFAULT nextval('public.consulta_concod_seq'::regclass);
 >   ALTER TABLE public.consulta ALTER COLUMN concod DROP DEFAULT;
       public               postgres    false    236    235    236            �           2604    17409    internacao prontuario_id    DEFAULT     �   ALTER TABLE ONLY public.internacao ALTER COLUMN prontuario_id SET DEFAULT nextval('public.internacao_prontuario_id_seq'::regclass);
 G   ALTER TABLE public.internacao ALTER COLUMN prontuario_id DROP DEFAULT;
       public               postgres    false    237    238    238            �           2604    17432    med_consulta id    DEFAULT     r   ALTER TABLE ONLY public.med_consulta ALTER COLUMN id SET DEFAULT nextval('public.med_consulta_id_seq'::regclass);
 >   ALTER TABLE public.med_consulta ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    240    239    240            �           2604    17483    paciente paciid    DEFAULT     r   ALTER TABLE ONLY public.paciente ALTER COLUMN paciid SET DEFAULT nextval('public.paciente_paciid_seq'::regclass);
 >   ALTER TABLE public.paciente ALTER COLUMN paciid DROP DEFAULT;
       public               postgres    false    244    243    244            �           2604    17386    visita visicod    DEFAULT     p   ALTER TABLE ONLY public.visita ALTER COLUMN visicod SET DEFAULT nextval('public.visita_visicod_seq'::regclass);
 =   ALTER TABLE public.visita ALTER COLUMN visicod DROP DEFAULT;
       public               postgres    false    234    233            �           2604    17440    visitante visicod    DEFAULT     v   ALTER TABLE ONLY public.visitante ALTER COLUMN visicod SET DEFAULT nextval('public.visitante_visicod_seq'::regclass);
 @   ALTER TABLE public.visitante ALTER COLUMN visicod DROP DEFAULT;
       public               postgres    false    241    242    242            ]          0    17277    atualiza_itens 
   TABLE DATA           �   COPY public.atualiza_itens ("Atualiza_ID", "Estoque_ID", "Prod_Cod", "Qtde_Anterior", "Qtde_Nova", "Data_Atualizacao") FROM stdin;
    public               postgres    false    225   +^       c          0    17347    atualiza_produtos 
   TABLE DATA           z   COPY public.atualiza_produtos ("AtuRec_ID", "Doacao_ID", "ProdCod", "DataAtualizacao", "ProdQtde", "FuncCod") FROM stdin;
    public               postgres    false    231   H^       b          0    17342    atualiza_receita 
   TABLE DATA           �   COPY public.atualiza_receita ("AtuRec_ID", "Receita_ID", "ProdCod", "DataAtualizacao", "TipoAtualizacao", "FuncCod") FROM stdin;
    public               postgres    false    230   e^       [          0    17268    caixa 
   TABLE DATA           J   COPY public.caixa ("Caixa_ID", "Saldo", "Ultima_Atualizacao") FROM stdin;
    public               postgres    false    223   �^       Y          0    17262    compra 
   TABLE DATA           ]   COPY public.compra ("Compra_ID", "FuncCod", "Compra_Data", "Compra_Valor_Total") FROM stdin;
    public               postgres    false    221   �^       h          0    17395    consulta 
   TABLE DATA           Q   COPY public.consulta (concod, medid, paciconvenio, contipo, condata) FROM stdin;
    public               postgres    false    236   �^       _          0    17324    contas 
   TABLE DATA           g   COPY public.contas ("Conta_ID", "Conta_Tipo", "Conta_Data", "Conta_Valor", "Conta_Status") FROM stdin;
    public               postgres    false    227   4_       `          0    17329    despesas 
   TABLE DATA           a   COPY public.despesas ("Despesa_ID", "Despesa_Tipo", "Despesa_Valor", "Despesa_Data") FROM stdin;
    public               postgres    false    228   Q_       ^          0    17283    doacao 
   TABLE DATA           Q   COPY public.doacao ("Doacao_ID", "Uso_ID", "FuncCod", "Data_Doacao") FROM stdin;
    public               postgres    false    226   n_       V          0    17241    funcionario 
   TABLE DATA           �   COPY public.funcionario ("FuncCod", "FuncNome", "FuncCPF", "FuncCargo", "FuncRG", "FuncEndereco", "FuncTelefone", "FuncData_Admissao", "FuncData_Demissao") FROM stdin;
    public               postgres    false    218   �_       j          0    17406 
   internacao 
   TABLE DATA           U   COPY public.internacao (prontuario_id, pacinome, dataentrada, datasaida) FROM stdin;
    public               postgres    false    238   �_       l          0    17429    med_consulta 
   TABLE DATA           �   COPY public.med_consulta (id, medcrm, mednome, medespecialidade, condata, horarioconsulta, pacinome, pacicpf, paciconvenio, numconvenio, dataultimaconsulta, contipo) FROM stdin;
    public               postgres    false    240   4`       W          0    17250    medico 
   TABLE DATA           s   COPY public.medico ("MedID", "MedNome", "MedCRM", "Med_Horario_Atendimento", "FuncCod", "MedTelefone") FROM stdin;
    public               postgres    false    219   �`       d          0    17352    movi_compra_prod 
   TABLE DATA           d   COPY public.movi_compra_prod ("Compra_ID", "ProdCod", "ProdQtde", "Compra_Valor_Total") FROM stdin;
    public               postgres    false    232   a       Z          0    17265    movimentacao_caixa 
   TABLE DATA           |   COPY public.movimentacao_caixa ("MoviC_ID", "MoviC_Tipo", "MoviC_Valor", "MoviC_Data", "Compra_ID", "Caixa_ID") FROM stdin;
    public               postgres    false    222   a       p          0    17480    paciente 
   TABLE DATA           �   COPY public.paciente (paciid, pacinome, pacicpf, paciconvenio, pacidata_internacao, pacidata_alta, paciendereco, pacirg, pacitelefone, pacidata_nascimento, pacicep) FROM stdin;
    public               postgres    false    244   ;a       \          0    17274    produtos 
   TABLE DATA           }   COPY public.produtos ("ProdCod", "ProdNome", "ProdQtde", "ProdCategoria", "ProdDescricao", "ProdEstoque_Minimo") FROM stdin;
    public               postgres    false    224   ,b       X          0    17253 
   prontuario 
   TABLE DATA           f   COPY public.prontuario ("Prontuario_ID", "MedCRM", "PaciNome", "PaciID", "Data_Abertura") FROM stdin;
    public               postgres    false    220   Ib       a          0    17334    receita 
   TABLE DATA           \   COPY public.receita ("Receita_ID", "MedID", "PaciID", "FuncCod", "DataReceita") FROM stdin;
    public               postgres    false    229   fb       U          0    17238    usuario 
   TABLE DATA           \   COPY public.usuario ("Usu_Tipo", "Usu_login", "Usu_Senha", "Usu_Data_Registro") FROM stdin;
    public               postgres    false    217   �b       e          0    17382    visita 
   TABLE DATA           ^   COPY public.visita (visicod, visinome, visicpf, visidata_entrada, visidata_saida) FROM stdin;
    public               postgres    false    233   �b       n          0    17437 	   visitante 
   TABLE DATA           �   COPY public.visitante (visicod, visinome, visicpf, visiemail, visitelefone, visiendereco, visicep, visidata_cadastro, visidata_nascimento) FROM stdin;
    public               postgres    false    242   c       }           0    0    consulta_concod_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.consulta_concod_seq', 12, true);
          public               postgres    false    235            ~           0    0    internacao_prontuario_id_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.internacao_prontuario_id_seq', 8, true);
          public               postgres    false    237                       0    0    med_consulta_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.med_consulta_id_seq', 2, true);
          public               postgres    false    239            �           0    0    paciente_paciid_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.paciente_paciid_seq', 15, true);
          public               postgres    false    243            �           0    0    visita_visicod_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.visita_visicod_seq', 14, true);
          public               postgres    false    234            �           0    0    visitante_visicod_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.visitante_visicod_seq', 25, true);
          public               postgres    false    241            �           2606    17400    consulta consulta_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.consulta
    ADD CONSTRAINT consulta_pkey PRIMARY KEY (concod);
 @   ALTER TABLE ONLY public.consulta DROP CONSTRAINT consulta_pkey;
       public                 postgres    false    236            �           2606    17411    internacao internacao_pkey 
   CONSTRAINT     c   ALTER TABLE ONLY public.internacao
    ADD CONSTRAINT internacao_pkey PRIMARY KEY (prontuario_id);
 D   ALTER TABLE ONLY public.internacao DROP CONSTRAINT internacao_pkey;
       public                 postgres    false    238            �           2606    17434    med_consulta med_consulta_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.med_consulta
    ADD CONSTRAINT med_consulta_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.med_consulta DROP CONSTRAINT med_consulta_pkey;
       public                 postgres    false    240            �           2606    17487    paciente paciente_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.paciente
    ADD CONSTRAINT paciente_pkey PRIMARY KEY (paciid);
 @   ALTER TABLE ONLY public.paciente DROP CONSTRAINT paciente_pkey;
       public                 postgres    false    244            �           2606    17388    visita visita_pkey 
   CONSTRAINT     U   ALTER TABLE ONLY public.visita
    ADD CONSTRAINT visita_pkey PRIMARY KEY (visicod);
 <   ALTER TABLE ONLY public.visita DROP CONSTRAINT visita_pkey;
       public                 postgres    false    233            �           2606    17442    visitante visitante_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY public.visitante
    ADD CONSTRAINT visitante_pkey PRIMARY KEY (visicod);
 B   ALTER TABLE ONLY public.visitante DROP CONSTRAINT visitante_pkey;
       public                 postgres    false    242            �           2606    17444    visitante visitante_visicpf_key 
   CONSTRAINT     ]   ALTER TABLE ONLY public.visitante
    ADD CONSTRAINT visitante_visicpf_key UNIQUE (visicpf);
 I   ALTER TABLE ONLY public.visitante DROP CONSTRAINT visitante_visicpf_key;
       public                 postgres    false    242            ]      x������ � �      c      x������ � �      b      x������ � �      [      x������ � �      Y      x������ � �      h   h   x�3�4����uu��/��K�4202�50�56�24 �:9��;���Zrrq����*;���rz敤�%^~xq>� C.C#�A���vr��qqq �� �      _      x������ � �      `      x������ � �      ^      x������ � �      V      x������ � �      j   |   x�3�tN,.�L�4202�50�54�3���8CR�KR��p���1D���i�eH�!4r�r��*����7�t-E��4�ʜ3(����Ic.NǼ����|� �U��\1z\\\ 92�      l   �   x�M�A
�0EדS�	�i�$˪+A�v�f���غ�:ŋY���χ��G@���CQ�1�_��cz0�1�|���Jm%Z@'.��>/O�����5 ���６��D����Ӕ�|��%�\���A���ڍ��R6块�-A0�����%�� x�/k      W      x������ � �      d      x������ � �      Z      x������ � �      p   �   x���=j�@��zu�-m�,3�?el�H����.͂6� ˰�s��"� 	�@������O�)�r��\�e��r�*�d����k�F� h��t�-����9��<��Q�4m�S�C9�m�lS-i#5�e��5/y�	�ǃpVat�-C+�k��-Oh `�H��!'v��ג&)�fE�J��b�?��q�r���r-+�N�����=��9 i ��ySM�|��^�      \      x������ � �      X      x������ � �      a      x������ � �      U      x������ � �      e   k   x�=�;
�0 �Y:�/ !�6��d�ҡS)�$����loyj0��8{�i�}��f�*������B�VҊ����tOCz�<�g_ �qq�V�7_��G5��/F���      n      x������ � �     